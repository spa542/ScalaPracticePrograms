package demo.part4Implicits

object PimpMyLibrary extends App {

  // Enrichments allow us to decorate existing classes with additional classes and properties

  // 2.isPrime
  // Implicit classes must take only one argunent
  implicit class RichInt(val value: Int) extends AnyVal { // For memory and compiler optimizations (extends AnyVal)
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    // Exercise 2
    def times(func: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          func()
          timesAux(n - 1)
        }

      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }
  new RichInt(42).sqrt

  // Can do this because of implicit class (type-enrichment)
  42.isEven // new RichInt(42).isEven

  // Type-Enrichment => Pimping "pimp my library"

  1 to 10

  import scala.concurrent.duration._
  3.seconds

  // Cannot do this
  // This is because it cannot do multiple enrichments in a row
  //42.isOdd

  // Exercise:
  // 1. Enrich the String class
  // - asInt
  // - encrypt
  // John -> lnjp (caeser cipher of 2)
  // 2. Keep enriching the Int class
  // times(function)
  // 3.times(() => ...)
  // - *
  // 3 * List(1,2) => List(1,2,1,2,1,2)

  // 1
  implicit class RichString(val value: String) extends AnyVal {
    def asInt: Int = Integer.valueOf(value)
    def encrypt(count: Int): String = value.map { c: Char =>
      val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
      val currChar = alphabet.indexOf(c.toUpper)
      if (currChar == -1) {
        c
      } else {
        alphabet((currChar + ((count + alphabet.length) % alphabet.length)) % alphabet.length).toLower
      }
    }
  }

  println("250".asInt + 4)
  println("Hi there".encrypt(20))

  // 2
  3.times(() => println("Scala rocks!"))
  println(4 * List(1,2))

  // "3" / 4
  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6" / 2) // This shouldnt be legal => stringToInt("6") / 2

  // Equivalent: implicit class RichAltInt(value: Int)
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  // DANGER zone
  implicit def intToBoolean(i: Int): Boolean = i == 1 // SUPER HARD to trace errors with implicit def of functions

  /*
    if (n) do something
    else do something else
   */

  val aConditionedValue = if (3) "OK" else "Something wrong"
  println(aConditionedValue)

  // Best Practices
  /*
    * keep type enrichment to implicit classes and type classes
    * avoid implicit defs as mcuh as possible
    * package implicits clearly, bring into scope ONLY what you need
    * IF you need conversions, make them specific
   */

}
