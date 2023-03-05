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

  // If we have two distinct or potentially unrelated types have equalizers implemented, we can call Equal on them regardless of type****
  // Compiler takes care to reach the correct type

  // Part 3 - Enrichment
  implicit class HTMLEnrichment[T](value: T) {
    // Implicit parameter will take another implicit type
    def toHTML2(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  // Tries to wrap john into whatever class has the method to be implemented
  println(john.toHTML2(UserSerializer)) // println(new HTMLEnrichment[User](john).toHTML(UserSerializer))
  println(john.toHTML2) // COOL!

  // We can extend the functionality to new types
  println(2.toHTML2)

  // We can also have different implementations for the same type (choose implementation)
  // Importing the specific serializer or defining it implicitly
  println(john.toHTML2(PartialUserSerializer))

  /* Enhancing types with a type class!
    - type class itself HTMLSerializer[T] { ... }
    - type class instances (some of which are implicit) UserSerializer, IntSerializer
    - conversion with implicit classes HTMLEnrichment
   */

  // Context Bounds
  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body> ${content.toHTML2(serializer)}</body></html>"

  // Write in a sweeter way!
  // This is telling the compiler to inject an implicit serializer as the second argument, however,
  // we cannot reference serializer by name!
  def htmlSugar[T: HTMLSerializer](content: T): String = {
    // Get the serializer
    val serializer = implicitly[HTMLSerializer[T]]
    // Now we can use the serializer by name!
    s"<html><body> ${content.toHTML2(serializer)}</body></html>"
  }

  // Implicitly
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // In some other part of the code we want to surface out what is the implicit val for permissions
  val standardPerms = implicitly[Permissions]

  // Takeaways
  /*
    * Type class
    * Type class instances (often implicit)
    * Invoking type class instances
    * Enriching types with type classes
   */

}
