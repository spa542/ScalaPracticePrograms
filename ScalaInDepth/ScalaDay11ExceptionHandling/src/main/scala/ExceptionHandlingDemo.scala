import java.io.FileReader // For reading files
import java.io.FileNotFoundException // For exception handling
import java.io.IOException // For exception handling
import scala.util.Try // Try class for exception handling
import scala.util.Success // Success class ^
import scala.util.Failure // Failure class ^
import scala.util.control.Exception.catching // For another exception handling

object ExceptionHandlingDemo {

  def main(args: Array[String]): Unit = {
    // First Example try catch
    try {
      val f = new FileReader("input.txt") // This file does not exist
    } catch {
      case e: FileNotFoundException => {
        println("File is not found - Exception Received")
      }
      case e: IOException => {
        println("Error while reading the file - Exception Received")
      }
      case _: Exception => { // for catching all exceptions
        println("Catch all unhandled exceptions")
      }
    }
    // Second Example try catch finally
    try {
      val a = 10 / 0
    } catch {
      case e: ArithmeticException => {
        println("Arithmetic Exception Received")
      }
    } finally {
      println("This is from the finally block!")
    }
    // Try Success Failure example
    val a = Try(10 / 0) // Wrap the code in the class
    a match {
      case Success(value) => println(value)
      case Failure(exception) => errorHandlingFunction(exception)
    }
    // catching example
    val catchExceptions = catching(classOf[ArithmeticException]).withApply(e => println("Arithmetic Exception has occurred using catching"))
    val b = catchExceptions(10 / 0)
  }

  // Can create your own error handling function
  def errorHandlingFunction(exception: Throwable): Unit = {
    println(exception)
    if (exception.toString.equals("java.lang.ArithmeticException: / by zero")) {
      println("Hello arithmetic exception!")
    }
  }

}
