package demo.ASandFPpart1

import scala.util.Try

object DarkSyntaxSugar extends App {

  // Syntax Sugar #1: Methods with Single Param
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  val description = singleArgMethod {
    // write some code
    42 // This value will be whats passed to the function
  }

  val aTryInstance = Try { // Reminiscent to Java Try {...}
    throw new RuntimeException
  }

  List(1,2,3).map { x =>
    x + 1
  }

  // Syntax Sugar #2: Single Abstract Method
  // Instances of traits with a single method can actually be reduced to lambdas
  trait Action {
    def act(x: Int): Int
  }

  // Normal way
  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }
  // Single Abstract Method
  val aFunkyInstance: Action = (x: Int) => x + 1 // magic

  // Example: Runnables
  // Instances of a trait/interface that can be passed on to threads
  // Normal way (java)
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello, Scala")
  })
  // Better way (Anonymous)
  val aSweeterThread = new Thread(() => println("Sweet Scala!"))

  // Pattern also works for classes that have some methods implemented but only have one method unimplemented
  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }
  // Assign a lambda to this abstract type
  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")

  // Syntax Sugar #3: The :: and #:: methods are special
  val prependedList = 2 :: List(3,4)
  // infix methods are actually converted to first object, .method, and then second thing
  // 2.::(List(3,4))
  // List(3,4).::(2)

  // Scala Specification
  // The associativity of a method is determined by an object's last character
  1 :: 2 :: 3 :: List(4,5)
  List(4,5).::(3).::(2).::(1)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // actual implementation here
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int] // end in a colon, therefore right associative

  // Syntax Sugar #4: Multi-word Method Naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet!"

  // Syntax Sugar #5: Infix Types
  class Composite[A, B]
  // Normal
  //val composite: Composite[Int, String] = println("hi")
  // Infix
  //val infixComposite: Int Composite String = println("hi 2")
  // Another Example
  class -->[A, B]
  //val towards: Int --> String = println("hi 3")

  // Syntax Sugar #6: update() is very special, much like apply()
  val anArray = Array(1,2,3)
  anArray(2) = 7 // Rewritten to anArray.update(2, 7) (takes two parameters and returns Unit)
  // Used in mutable collections
  // Remember apply() AND update()

  // Syntax Sugar #7: Setters for Mutable Containers
  class Mutable {
    private var internalMember: Int = 0 // private for object oriented encapsulation
    def member = internalMember // "getter"
    def member_=(value: Int): Unit =
      internalMember = value // "setter"
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // Scala compiler will rewrite this properly (aMutableContainer.member_=(42)

}
