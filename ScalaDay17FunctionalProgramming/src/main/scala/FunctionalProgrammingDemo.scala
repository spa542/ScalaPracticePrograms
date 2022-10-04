import java.util.Date // For date object

object FunctionalProgrammingDemo {

  def main(args: Array[String]): Unit = {
    // Functions get called from most inner to most outer
    println(s"Main Function: ${exec(time())}")

    // Partially Applied functions
    var date = new Date()
    // Creating a partially applied function here
    val logWithDate = log(date, _:String) // Will only have to pass a string when calling since date is consistent from call to call
    //log(date, "Hello 1")
    logWithDate("Hello 1")
    Thread.sleep(2000)
    //log(date, "Hello 2")
    logWithDate("Hello 2")
    Thread.sleep(2000)
    //log(date, "Hello 3")
    logWithDate("Hello 3")

    // Nested functions
    def printHello(msg: String): Unit = {
      println(s"Hello $msg")
    }
    printHello("Ryan")

    // Carrying functions
    val str1 = "Hello"
    val str2 = "World"
    printSomething(str1)(str2)

    // Carrying function with Partially Applied function
    val sum = add2(29)_
    println(sum(5))
  }

  def time(): Long = {
    println("Inside time function...")
    System.nanoTime()
  }

  def exec(t: Long): Long = {
    println("Entering exec function...")
    println(s"Time: ${t}")
    println("Exiting from exec function...")
    t
  }

  def log(date: Date, msg: String): Unit = {
    println(msg + " " +  date)
  }

  def printSomething(str1: String) (str2: String) = str1 + " " + str2;

  def add2(a: Int) (b: Int) = a + b;

}
