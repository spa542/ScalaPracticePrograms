package demo.part2AFP

import sun.font.AttributeMap

object Monads extends App {

  /*
  Intro
  Monads are a kind of types which have some fundamental operations.
  Two Functions:
   - pure or apply
   - bind
  List, Option, Try, Future, Stream, Set are all monads

  Operations must satisfy the monad laws:
   - left-identity => unit(x).flatMap(f) == f(x)
   - right-identity => aMonadInstance.flatMap(unit) == aMondaInstance
   - associativity => m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
   */

  // Our own try monad

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }
  object Attempt {
    // Don't want this argument to be evaluated when we build the attempt because the evaluation
    // of the parameter could throw exceptions
    // Value will only get evaluated when it is used
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  // Prove Monad Laws
  // 1. left-identity
  // unit.flatMap(f) = f(x)
  // Attempt(x).flatMap(f) = f(x) // Success case!
  // Success(x).flatMap(f) = f(x) // Proved
  // 2. right-identity
  // attempt.flatMap(unit) = attempt
  // Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x) // Proved
  // Fail(_).flatMap(...) = Fail(e)
  // 3. associativity
  // attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
  // Fail(e).flatMap(f).flatMap(g) = Fail(e)
  // Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)
  // Success(v).flatMap(f).flatMap(g) = f(v).flatMap(g) OR Fail(e)
  // Success(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g) OR Fail(e) // Proved

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)

  // Exercise
  // 1. Implement a Lazy[T] monad = computation which will only be executed when it's needed
  // unit/apply
  // flatMap
  // 2. Monads = unit + flatMap
  // Monads = unit + map + flatten
  // Monad[T] {
  //   def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
  //   def map[B](f: T => B): Monad[B] = ???
  //   def flatten(m: Monad[Monad[T]]): Monad[T] = ???
  // }

  // 1. The Lazy Monad
  // => A prevents value from being evaluated while object is being constructed
  class Lazy[+A](value: => A) {
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue
    // Will delay the value parameter being evaluated as well
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    // Should not see this lyric because it is lazy evaluated
    println("Today I don't feel like doing anything")
    42
  }
  // Should see both the lyric and value printed out
  println(lazyInstance.use)

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  flatMappedInstance.use
  flatMappedInstance2.use

  /*
  left-identity
  Lazy(v).flatMap(f) = f(v)
  right-identity
  Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)
  associativity
  Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
  */

  // 2. map and flatten in terms of flatMap
  // Monad[T] {
  //   def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
  //   def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
  //   def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)
  // }

  // List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
  // List(List(1,2), List(3,4)).flatten = List(List(1,2), List(3,4)).flatMap(x => x)

}
