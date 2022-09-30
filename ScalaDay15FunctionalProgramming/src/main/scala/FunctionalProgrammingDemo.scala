object FunctionalProgrammingDemo {

  def main(args: Array[String]): Unit = {
    // Call the sum function
    var test1 = sum(2, 3)
    println(test1)

    // With named arguments
    var test2 = sum(y=3, x=2)
    println(test2)

    // Printing with multiple arguments
    multipleArgs(5, "Hello There", "India", "US")

    // Default parameters example
    defaultParam()

    // Recursive functions example
    println(s"Factorial of 5 is ${recursiveFactorial(5)}")

  }

  def sum(x: Int, y: Int): Int = {
    x + y
  }

  def multipleArgs(num: Int, args: String*): Unit = {
    for (arg <- args) {
      println(arg * num)
    }
  }

  def defaultParam(x: Int = 10, y: Int = 5): Unit = {
    println(s"Value of x is ${x} and value of y is ${y}")
  }

  def recursiveFactorial(x: Int): Int = {
    if (x <= 1) {
      return 1
    } else {
      x * recursiveFactorial(x - 1)
    }
  }

}
