
object StringsDemo {

  def main(args: Array[String]): Unit = {
    // Basic String Example
    val greetings = "Hello World" // This is auto-interpreted by the double quotes
    println(greetings)
    // Getting Length
    val lengthOfString = greetings.length()
    println(s"Length of the string \"${greetings}\" is ${lengthOfString}")
    // Concat Method
    var var1 = "Hello "
    var var2 = "World"
    var concatMe = var1.concat(var2) // You can concat using the + operator as well just like Java
    println(s"The concatenation of \"${var1}\" and \"${var2}\" is \"${concatMe}\"")
    // Equals Method
    var test1 = ""
    var test2 = ""
    if (test1 == test2 && test1.equals(test2)) { // Showcasing both ways
      println("Equal!")
    }
    // String Formatting
    var nameOfCar = "Mercedes"
    var costOfCar = 500000
    var mileageOfCar = 8.5
    println("Name of car is %s and cost of car is %d and mileage of car is %f".format(nameOfCar, costOfCar, mileageOfCar)) // Can do it this way (pythonic)
    printf("Name of car is %s and cost of car is %d and mileage of car is %f", nameOfCar, costOfCar, mileageOfCar) // Can do it this way (printf way)
    println(s"Name of car is ${nameOfCar} and cost of car is ${costOfCar} and mileage of car is ${mileageOfCar}") // Can do it this way (Scala way)
    // Multiline String
    var multilineString =
      """Hello,
        |How
        |are
        |You
        |""".stripMargin // Just like python with some extra quirks
    println(s"Multiline string is ${multilineString}")
    // String Interpolation
    // "s" Interpolator
    println(s"Hello $nameOfCar") // "s" String Interpolation for single variables
    println(s"Hello ${costOfCar / mileageOfCar}") // "s" String Interpolation with math equations / multi variables
    // "f" Interpolator
    var name = "Ryan"
    var salary = 20000.2
    println(f"Name is $name%s and salary is $salary%8.2f and designation is PM") // "f" String Interpolation
    // raw Interpolator
    print(raw"Hello World\n How are you?")
    // Many other methods that can be used (all in Java)
  }

}
