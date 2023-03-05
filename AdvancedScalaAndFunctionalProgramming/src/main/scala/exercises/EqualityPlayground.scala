package exercises

import demo.part4Implicits.TypeClasses.User

object EqualityPlayground extends App {

  /*
    Implement Equality Type Class
    - has a method called equal that compare two values
    - implement two instances that compare users by name and both name and email
   */
//  trait Equality[A, B] {
//    def equal(value1: A, value2: B): Boolean
//  }
//
//  object UserNameEquality extends Equality[User, User] {
//    override def equal(user1: User, user2: User): Boolean = user1.name == user2.name
//  }
//
//  object UserEmailEquality extends Equality[User, User] {
//    override def equal(user1: User, user2: User): Boolean = user1.email == user2.email
//  }

  // Type Class
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object Equal {
    // For exercise 2
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(a, b)
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.email == b.email
  }

  // Exercise
  // TypeSafeEqual
  implicit class ImplicitConversionClassEquality[T](value: T) {
    def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = equalizer(value, anotherValue)
    def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = !equalizer(value, anotherValue)
  }

  val john = User("John", 32, "john@ryry.com")
  val anotherJohn = User("John", 45, "anotherJohn@ryry.com")
  println(Equal(john, anotherJohn))
  // Exercise: implement the type class pattern for the equality type class
  println(Equal.apply(john, john))
  println(Equal[User](john, john))
  // AD-HOC Polymorphism
  println(Equal(john, john)) // AD-HOC polymorphism

  // Exercise - Improve the Equal Type Class with an implicit conversion class
  // ===(another value: T)
  // !==(another value: T)
  println(john.===(anotherJohn))
  println(john === anotherJohn)
  println(john.!==(anotherJohn))
  println(john !== anotherJohn)
  /*
    john.===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)
   */
  /*
    TYPE-SAFE!!!
   */
  println(john == 42)
  //println(john === 43) // Type-safe!
  // Compiler prevents me from even compiling!

}
