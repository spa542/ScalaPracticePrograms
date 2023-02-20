package demo.part2AFP

object LazyEvaluation extends App {

  // Lazy values are evaluated once but only executed when the value is actually used
  // lazy DELAYS the evaluation of values
  lazy val x: Int = throw new RuntimeException()
  // Will crash the program here
  // println(x)

  // Lazy values evaluate only once and keep the ending value for the remainder of the program
  lazy val y: Int = {
    println("hello")
    42
  }
  println(y)
  println(y)

  // Examples of Implications
  // Side Effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  // Runtime is smart enough to short circuit the && statement
  // sideEffectCondition will not be evaluated here
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  // In conjunction with call by name
  def byNameMethod(n: => Int): Int = {
    // CALL BY NEED
    lazy val t = n // only evaluated once
    t + t + t + 1
  }
  def retrieveMagicValue = {
    // side effect or long computation
    println("waiting...")
    Thread.sleep(1000)
    42
  }

  // Use lazy vals
  println(byNameMethod(retrieveMagicValue))

  // Filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }
  def greaterThan20(i: Int): Boolean = {
    println(s"$i greater than 20?")
    i > 20
  }

  val numbers = List(1,25,40,25,40,5,23)
  val lt30 = numbers.filter(lessThan30) // List(all but 40)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // uses lazy values under the hood
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20)
  println
  println(gt20Lazy) // no side effects being applied!
  gt20Lazy.foreach(println) // watch the ordering in the console here!!! (filtering being checked on a by need basis!!)

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1,2,3) if a % 2 == 0 // use lazy vals
  } yield a + 1
  // Equivalent to...
  // List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

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
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of this stream
    def takeAsList(n: Int): List[A]
  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }

  // See answer in StreamsPlayground!!!



}
