package demo.part4Implicits

object TypeClasses extends App {

  trait HTMLWritable {
    def toHTML: String
  }

  // Option 1 - Inherit Trait Directly
  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  // Works but has two big disadvantages
  val john = User("John", 32, "john@rockit.com")
  john.toHTML
  /*
    1. Only works for the types WE write
    2. This is only ONE implementation out of many (would need to supply multiple implementations)
   */

  // Option 2 - Patter Matching
  object HTMLSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(n, a, e) => println("hi")
      //case java.util.Date => println("hello")
      case _ => println("yo")
    }
  }
  // Disadvantages
  // 1. Lost the type safety
  // 2. Would need to modify this code EVERY time we want to make a change
  // 3. STILL one implementation

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  object UserSerializer extends HTMLSerializer[User] { // for users logged in (see use case in step 2)
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  println(UserSerializer.serialize(john))
  // Advantages
  // 1. We can define serializers for other types (even for types that we haven't written ourselves)
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString()}</div>"
  }
  // 2. We can define Multiple serializers for any given type
  object PartialUserSerializer extends HTMLSerializer[User] { // for users not logged in
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  // HTMLSerializer is called a Type Class (type level) (**Regular classes are on the object level)
  // Describes a collection or properties or methods that a TYPE must have in order to belong to that specific type class
  // All implementers of a type class are called type class instances

  // TYPE CLASS TEMPLATE
  trait MyTypeClassTemplate[T] {
    def action(value: T): String // or some other type
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

  /*
    Implement Equality Type Class
    - has a method called equal that compare two values
    - implement two instances that compare users by name and both name and email
   */
  trait Equality[A, B] {
    def equal(value1: A, value2: B): Boolean
  }

  object UserNameEquality extends Equality[User, User] {
    override def equal(user1: User, user2: User): Boolean = user1.name == user2.name
  }

  object UserEmailEquality extends Equality[User, User] {
    override def equal(user1: User, user2: User): Boolean = user1.email == user2.email
  }

  // Other simpler solution
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

  // Part 2 - Providing implicit type class instances by implicit values and parameters
  // Add companion object to HTML Serializer
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }

  implicit object UserSerializer2 extends HTMLSerializer[User] { // for users logged in (see use case in step 2)
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  //println(HTMLSerializer.serialize(42)(IntSerializer)) // without implicit
  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))
  // We can call HTMLSerializer for ANY type that we have a corresponding implicit serializer for
  // Access the entire type class interface
  println(HTMLSerializer[User].serialize(john))

  // Exercise: implement the type class pattern for the equality type class
  println(Equal.apply(john, john))
  println(Equal[User](john, john))
  println(Equal(john, john)) // AD-HOC polymorphism
  // If we have two distinct or potentially unrelated types have equalizers implemented, we can call Equal on them regardless of type****
  // Compiler takes care to reach the correct type


}
