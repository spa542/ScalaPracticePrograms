package exercises

import scala.annotation.tailrec


// Exercise
// Implement a lazily evaluated singly linked stream of elements
// naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!!!)
// naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals (finite stream!!!)
// naturals.foreach(println) // will crash - infinite!!
// naturals.map(_ * 2) // stream of all even numbers (potentially infinite)
abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](elem: B): MyStream[B] // prepend operator
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] // concatenate two streams

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // takes the first n elements out of this stream
  def takeAsList(n: Int): List[A] = take(n).toList()

  // [1 2 3].toList([]) =
  // [2 3].toList([1]) =
  // [3].toList([2 1]) =
  // [].toList([3 2 1]) =
  // [1 2 3]
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)
}

// https://docs.scala-lang.org/tour/variances.html
// INVARIANCE
// By default, type parameters in Scala are invariant: subtyping relationships between the
// type parameters aren’t reflected in the parameterized type.
// Because the abstract class is covariant, we can implement with Nothing type
// COVARIANCE
// More formally, that gives us the following relationship: given some class Cov[+T],
// then if A is a subtype of B, Cov[A] is a subtype of Cov[B].
// This allows us to make very useful and intuitive subtyping relationships using generics.
object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](elem: B): MyStream[B] = new Cons(elem, this)
  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream

  def foreach(f: Nothing => Unit): Unit = () // return the unit ( () )
  def map[B](f: Nothing => B): MyStream[B] = this
  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this
}

// Call by name is needed to run the lazy evaluation
class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false
  // May need this more than once throughout the implementation
  override val head: A = hd
  override lazy val tail: MyStream[A] = tl // call by need

  // Tail will continue to be lazily evaluated (lazy evaluation preserved)
  // val s = new Cons(1, EmptyStream)
  // val prepended = 1 #:: s = new Cons(1, s)
  def #::[B >: A](elem: B): MyStream[B] = new Cons(elem, this)
  // Needs to be called by name or it will evaluate the expression first before any lazy evaluation
  // Will evaluate tail.flatMap first before evaluating HOF flatMap
  // We want to delay the evaluation of tail.flatMap(f) until it is needed
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream)

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  // s = new Cons(1, ?)
  // mapped = s.map(_ + 1) = new Cons(2, s.tail.map(_ + 1))
  // ... mapped.tail (if I use tail in later expression then s.tail.map will get evaluated)
  def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f)) // preserves lazy evaluation
  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)
  def filter(predicate: A => Boolean): MyStream[A] =
    if (predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate) // preserves lazy evaluation

  def take(n: Int): MyStream[A] =
    if (n <= 0) EmptyStream
    else if (n == 1) new Cons(head, EmptyStream)
    else new Cons(head, tail.take(n - 1))
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new Cons(start, MyStream.from(generator(start))(generator))
}


object StreamsPlayground extends App {
  // Lazy evaluated so no stack overflow!
  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals // naturals.#::(0)
  println(startFrom0.head)

  startFrom0.take(10000).foreach(println)

  // map, flatMap
  println(startFrom0.map(_ * 2).take(100).toList())
  println(startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList())
  println(startFrom0.filter(_ < 10).take(10).toList())
  // println(startFrom0.filter(_ < 10).toList()) // Does not guarantee that the filter is finite
  // println(startFrom0.filter(_ < 10).take(11).toList()) // Will crash due to infinitely trying to satisfy the predicate
  println(startFrom0.filter(_ < 10).take(10).take(20).toList()) // Works because we are taking 20 from a list of 10

  // Exercises on streams
  // 1 - stream of fibonacci numbers
  // 2 - stream of prime numbers with Eratosthenes' sieve
  // [2 3 ...]
  // filter out all numbers divisible by 2
  // [2 3 5 7 9 11 ...]
  // filter out all numbers divisible by 3
  // [2 3 5 7 11 13 17 ...]
  // filter out all numbers divisible by 5
  // and so on and so on.....

  // Fibonacci
  // [first, [ ...
  // [first, fibo(second, first + second)
  def fibonacci(first: Int, second: Int): MyStream[Int] =
    new Cons(first, fibonacci(second, first + second))

  println(fibonacci(1, 1).take(100).toList())

  // Eratosthenes
  // [2 3 4 5 6 7 8 9 10 11 12 ...
  // [2 3 5 7 9 11 13 ...
  // [2 eratosthenes applied to (numbers filtered by n % 2 != 0)
  // [2 [3 eratoshenes applied to (numbers filtered by n % 3 != 0)
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if (numbers.isEmpty) numbers
    else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))

  println(eratosthenes(MyStream.from(2)(_ + 1)).take(100).toList())
}
