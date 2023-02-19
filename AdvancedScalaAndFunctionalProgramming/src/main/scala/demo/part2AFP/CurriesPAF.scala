package demo.part2AFP

// Curries and Partially Applied Functions
object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  // Receives multiple parameters lists
  println(superAdder(3)(5)) // curried function

  // METHOD! (parts of instances of classes)
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  // Converted a method to a function of Int => Int
  // Does not work if we remove the type annotation
  val add4: Int => Int = curriedAdder(4) // Will return the function remaining after the first called function

  // What is done behind the scenes above
  // lifting (transforming a method to a function) = ETA-EXPANSION (Wrapping functions into extra layer while preserving original functionality)
  // **performed by the compiler
  // functions != methods (JVM limitations)
  def inc(x: Int) = x + 1
  List(1,2,3).map(inc) // ETA-Expansion

  // Partial Function Applications
  val add5 = curriedAdder(5) _ // Do an ETA-expansion for me!! (compiler) -> convert expression into an Int => Int function

  // Exercises
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above
  val add7 = (x: Int) => simpleAddFunction(7, x) // simplest
  val add7_2 = simpleAddFunction.curried(7)
  val add7_3 = curriedAddMethod(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF = alternative syntax
  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
              // y => simpleAddMethod(7, y)
  val add7_6 = simpleAddFunction(7, _: Int) // works as well

  // Underscores are Powerful (reduce function arity)
  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you?") // x: String => concatenator(hello, x, hru)
  println(insertName("Daniel"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String) // (x, y) => concatenator("Hello, ", x, y)
  println(fillInTheBlanks("Daniel - ", " Scala is awesome!"))

  // Exercises
  // 1. Process a list of numbers and return their string representations with different formats
  // use the %4.2f, %8.6f, and %14.12f with a curried formatter function
  println("%4.2f".format(Math.PI))
  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(simpleFormat))
  println(numbers.map(seriousFormat))
  println(numbers.map(preciseFormat))

  println(numbers.map(curriedFormatter("%14.12f"))) // compiler does sweet eta-expansion for us (no _)

  // 2. Difference between
  // - functions vs methods
  // - parameters: by-name vs 0-lambda
  // Calling byName and byFunction with the following expressions
  // - int
  // - method
  // - parenMethod
  // - lambda
  // - PAF
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  // Difference between accessor methods without parenthesis and proper methods with parenthesis****

  byName(23) // ok
  byName(method) // ok
  byName(parenMethod())
  byName(parenMethod) // ok but BEWARE == byName(parenMethod())
  //byName(() => 42) // not ok
  byName((() => 42)()) // ok
  //byName(parenMethod _) // not ok

  //byFunction(45) // not ok
  //byFunction(method) // not ok!!!!! compiler does not do eta-expansion here
  byFunction(parenMethod) // compiler does eta-expansion
  byFunction(() => 46) // ok
  byFunction(parenMethod _) // ok (underscore is unnecessary) (works but with warning)

}
