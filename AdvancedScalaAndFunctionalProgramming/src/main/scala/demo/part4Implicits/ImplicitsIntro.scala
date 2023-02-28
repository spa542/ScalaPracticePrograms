package demo.part4Implicits

object ImplicitsIntro extends App {

  // How does this compile?
  val pair = "Ryan" -> "555" // This is an implicit class
  // Implicits will turn first argument into a special class and then use the -> method

  // We know operators are functions of the first arg with the second arg as a parameter

  case class Person(name: String) {
    def greet = s"Hi, my name is $name!"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  // The compiler looks for all implicity classes, objects, and values that can help the computation
  println("Peter".greet) // println(fromStringToPerson("Peter").greet) (what the compile rewrites our code into!)
  // If there are multiple implicits that match, the compiler will give up***

  // Will not compile if uncommented
//  class A {
//    def greet: Int = 2
//  }
//  implicit def fromStringToA(str: String): A = new A

  // Implicit Parameters
  def increment(x: Int)(implicit amount: Int) = x + amount

  implicit val defaultAmount = 10

  // defaultAmount will be implicitly passed as the second parameter in the list
  increment(2)
  // Not the same thing as default arguments! -> Found by the compiler, rather than set at runtime




}
