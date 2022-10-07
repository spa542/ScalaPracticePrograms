object FunctionalProgrammingDemo {

  def main(args: Array[String]): Unit = {
    // Testing
    println(s"Normal way of defining a function ${increments(5)}")
    println(s"Using anonymous function to run ${increments2(5)}")
    printHelloWorld()

    // Practical anon function examples
    var listOfNumbers = List(1,2,3,4,5,6,7,8,9)
    listOfNumbers.foreach(num => if (num % 3 == 0) println(s"${num} is divisible by 3")) // Could skip second arg
    var divisibleByThreeOutput = listOfNumbers.filter(num => num % 3 == 0)
    divisibleByThreeOutput.foreach(println)
    var squareOfNumbers = listOfNumbers.map(square) // can also use _ ^ 2
    println(s"The square of the list of numbers is ${squareOfNumbers}")

    // Call by name functions examples (passing function as parameter) (same outer function but different function passed)
    printValues(increments, 2)
    printValues(decrements, 2)

  }

  // Defining function value
  def increments(x: Int): Int = {
    x + 1
  }

  def decrements(x: Int): Int = {
    x - 1
  }

  // Taking a function parameter
  def printValues(func: (Int) => Int, x: Int): Unit = {
    println(s"Addition value is ${func(x)}")
  }

  // Defining a function value / Anonymous function example 1
  var increments2 = (x: Int) => x + 1

  //  Anonymous function example 2 (no parameter and no return type)
  var printHelloWorld = () => println("Hello World!")

  var square = (x: Int) => x * x


}
