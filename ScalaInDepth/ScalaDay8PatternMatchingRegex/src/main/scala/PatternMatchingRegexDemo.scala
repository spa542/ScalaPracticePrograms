import scala.util.matching.Regex // For Regex

// Case class using pattern matching
case class Car(name:String, cost:Int) {

  def printDetails(): Unit = {
    println(s"Car Name: ${this.name}")
    println(s"Car Cost: ${this.cost}")
  }
}


object PatternMatchingRegexDemo {

  def matchPatternTest(matchMe:Any) = matchMe match {
    case 1 => "One"
    case 2 => "Two"
    case 3 => "Three FOO"
    case 4 => 4
    case 5 => "Five bar"
  }


  def main(args: Array[String]): Unit = {
    // Testing basic pattern matching
    println(matchPatternTest(1))
    println(matchPatternTest(2))
    println(matchPatternTest(3))
    println(matchPatternTest(4))
    println(matchPatternTest(5))
    // Testing Case Car Class
    val testCar = Car("test", 1000)
    testCar.printDetails()
    // Testing Matching with Case Class Car
    val mercedes = Car("Mercedes", 4000)
    val bmw = Car("BMW", 7000)
    val jaguar = Car("Jaguar", 100000)
    // Match Cars
    for (car <- List(mercedes, bmw, jaguar)) {
      car match {
        case Car("Mercedes", 4000) => println("Car is Mercedes, Congrats!")
        case Car("BMW", 7000) => println("Car is BMW, WOW!")
        case Car(name, cost) => println(s"Car is ${name} and cost is ${cost}") // Match any car object with this constructor
      }
    }
    // Testing Regex
    // There are a lot of different functions that are available, findFirstIn, findAllIn, matches, etc.
    // The string given to the regex class does not need to be a full regular expression (can find other string matches)
    val reg1 = new Regex("[0-9]*") // First way
    val reg2 = "[0-5]".r // Second way
    // Testing Regex
    println(reg1.matches("12345"))
    println(reg2.matches("4"))
    // Testing Special getOrElse Function
    val result = reg1.findFirstIn("1").getOrElse("No match found")
    println(result)
    val result2 = reg1.findFirstIn("hello").getOrElse("No match found")
    println(result2)
    // There are a lot of options when it comes to regular expression formatting (see Documentation)
  }

}
