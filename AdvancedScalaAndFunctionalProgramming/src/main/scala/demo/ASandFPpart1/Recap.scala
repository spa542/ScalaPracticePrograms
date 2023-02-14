package demo.ASandFPpart1

import scala.annotation.tailrec

// "extends App" Will make our code runnable from the ide directly without main function
object Recap extends App {

  // Basic Declaration
  val aCondition: Boolean = false
  val aConditionedVal = if (aCondition) 42 else 65
  // Instructions vs Expressions
  // Instructions are executed in sequence to do things
  // Scala is built on expressions that are built on top of each other

  // Code block expression
  // The value of a code block is the value of it's last expression
  val aCodeBlock = { // Compiler infers types for us
    if (aCondition) 54 // This number is useless
    56
  }

  // Unit (the type of expressions that do not return anything meaningful but do have some sort of side effect
  // - Equivalent to void
  val theUnit = println("hello, Scala")

  // Functions
  def aFunction(x: Int): Int = x + 1

  // Recursion: stack and tail
  // specify to the compiler that we have a tail recursion function here
  // *tail recursion functions do not require any additional stack frames when calling them recursively
  // tailrec converts recursive function to iterative in the bytecode
  @tailrec def factorial(n: Int, accumulator: Int): Int =
    if (n <= 0) accumulator
    else factorial(n - 1, n * accumulator)

  // Object-Oriented
  class Animal
  class Dog extends Animal
  // Subtyping polymorphism
  val aDog: Animal = new Dog

  // Can have methods that are or arent implemented
  // Methods that are not implemented will need to be implemented in whatever class inherits this trait
  trait Carnivore {
    def eat(a: Animal): Unit
  }
  // Overriding here
  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!!")
  }

  // Method Notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog // natural language

  // All operators are implemented as functions
  println(1.+(2))

  // Anonymous Classes (can instantiate the trait on the spot by implementing the required methods)
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar!")
  }

  // Generics
  // The + signifies covariance
  abstract class MyList[+A]

  // Singleton Objects and Companions
  object MyList // This is companion with MyList up above

  // Case Class
  // *many methods are already defined for us
  case class Person(name: String, age: Int)

  // Exceptions
  //val throwException = throw new RuntimeException // No Type (Nothing)
  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "I caught an exception!"
  } finally {
    println("Some logs in the finally...")
  }

  // Packaging and Imports

  // Functional Programming
  // Functions are instances of classes with apply methods
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)

  val anonymousIncrementer = (x: Int) => x + 1
  List(1,2,3).map(anonymousIncrementer) // higher order function (HOF)
  // map, flatMap, filter

  // for-comprehension
  // This is a chain of maps and flatMaps (possibly with some filters if there are if chains
  val pairs = for {
    num <- List(1,2,3) // if condition
    char <- List('a','b','c')
  } yield num + "-" + char

  // Scala Collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 555
  )

  // "Collections": Options, Try
  // More like abstract computations
  val anOption = Some(2)

  // Pattern Matching (One of the most powerful Scala features) (switch statement on steroids)
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)
  // Decomposing class variables
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }

  // All the Patterns!


}
