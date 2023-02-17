package demo.part2AFP

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  // Bad implementation
  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  // A better implementation
  // This is a total function (cannot be partial)
  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // {1,2,5} => Int (This is a partial function from Int => Int)

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value (sweeter version of example above)

  println(aPartialFunction(2))
  //println(aPartialFunction(25982)) // Will fail with a scala match error

  // Partial Function Utilities
  println(aPartialFunction.isDefinedAt(67)) // is this partial function defined for this argument??

  // lift
  val lifted = aPartialFunction.lift // Turns partial function into total function of Int => Option[Int]
  println(lifted(2))
  println(lifted(98))

  val partialFunctionChain = aPartialFunction.orElse[Int, Int] { // Can give a second partial function if first fails
    case 45 => 67
  }

  println(partialFunctionChain(2))
  println(partialFunctionChain(45))

  // Partial Functions extend normal functions

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1,2,3).map { // Will crash if one of the values is not part of the cases
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  // NOTE: Unlike functions which can have multiple parameters, partial functions can only have one parameter type

  // Exercises
  // 1 - Construct partial function instance yourself (anonymous class)
  // 2 - dumb chatbot as a partial function

  // **Without boilerplate
  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }

    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 5
  }

  val aChatBotPartialFunction: PartialFunction[String, String] = {
    case "hello" => "Hi there"
    case "yo" => "yo whats up!"
    case "woo" => "woot woot"
    case _ => "idk what you are saying....."
  }

  scala.io.Source.stdin.getLines().foreach { line =>
    println(aChatBotPartialFunction(line))
  }

  // Another way
  //scala.io.Source.stdin.getLines().map(aChatBotPartialFunction).foreach(println)
}
