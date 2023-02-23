package demo.part1ASandFP

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  // Complex Pattern (Special Case)
  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
    // equivalent
    case ::(head, Nil) => println(s"The only element is $head")
    case _ =>
  }

  // Structures for patthern matching
  // - constants
  // - wildcards
  // - case classes
  // - tuples
  // - some special magic like above case

  // Example: For some reason you cannot make this class a case class, but you still want to pattern match!
  class Person(val name: String, val age: Int)

  // Define companion object with unapply method returning an option of matching parameters
  // Can also be differently named singleton object
  object Person {
    def unapply(person: Person): Option[(String, Int)] = {
      if (person.age < 21) None
      else Some((person.name, person.age))
    }
    // Can overload unapply method
    def unapply(age: Int): Option[String] = Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(name, age) => s"Hi, my name is $name and I am $age years old."
    case _ => println("Incorrect match")
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
    case _ => println("error...")
  }

  println(legalStatus)

  // Exercise: Match against integers with special conditions
  val n: Int = 45
  // Bad example
  val matchProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "an even number"
    case _ => "no property"
  }
  println(matchProperty)
  // Correct example
  // Can do this without option for quick and easy tests
  // You can reuse these boolean tests
  // However, if you have a lot of conditions, things will get too verbose
  object even {
    def unapply(num: Int): Boolean = if (num % 2 == 0) true else false
  }
  object singleDigit {
    def unapply(num: Int): Boolean = if (num > -10 && num < 10) true else false
  }
  val matchPropertyCorrect = n match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }
  println(matchPropertyCorrect)

  // Infix patterns
  case class Or[A, B](a: A, b: B) // Either (similar to option)
  val either = Or(2, "two")
  // First two cases are exactly the same!
  val humanDescription = either match {
    case Or(number, string) => s"$number is written as $string"
    case number Or string => s"$number is written as $string"
    case _ => "none"
  }
  println(humanDescription)

  // Decomposing Sequences
  val vararg = numbers match {
    // Pattern matching against the whole list for the sequence
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  // Can name this object whatever we want
  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _) // Recursively call unapplySeq and then prepend the head back in the right spot
  }

  // This allows for variable pattern matching when you dont know what values are going to be inside the list
  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1 and 2"
    case _ => "something else"
  }

  println(decomposed)

  // Custom Return Types for unapply
  // isEmpty: Boolean, get: something
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }
  // The return type does not need to be an option, it just needs to be a class with isEmpty and get defined
  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false
      override def get = person.name
    }
  }

  println(bob match {
    case PersonWrapper(name) => s"This person's name is $name"
    case _ => "An Alien"
  })


}
