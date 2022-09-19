object HelloWorld {

  def main(args: Array[String]): Unit = {
    print("Hello World!\n")
    print("Hi there\n")
    println("Hello")

    var result = add(2, 5)
    println("\nThe result of 2 + 5 is " + result)
    var result2 = subtract(7, 2)
    println("The result of 7 - 2 is " + result2)
    var result3 = multiply(2, 4)
    println("The result of 4 * 2 is " + result3)
  }

  def add(x:Int, y:Int): Int = {
    var z = x + y
    return z // It is not necessary to specify the return keyword
  }

  // Second version without return keyword
  def subtract(x:Int, y:Int): Int = {
    var z = x - y
    z // Return this value
  }

  // Third version without the return type
  def multiply(x:Int, y:Int) = {
    var z = x * y
    z // If I specify the return type here there will be an error
  }

}
