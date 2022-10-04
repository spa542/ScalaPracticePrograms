object ClosuresDemo {

  def main(args: Array[String]): Unit = {
    println(multiplier(5))
    println(multiplier2(5))
  }

  /*
  def multiplier(x: Int): Int = {
    x * 10
  }
  */

  // If variable is defined outside of the function body scope, then it is called a closure
  var factor = 10

  var multiplier = (x: Int) => x * factor

  // If a variables used inside a function body is neither an input parameter nor defined inside the function body,
  // then it is a closure
  var factor1 = 20

  def multiplier2(x: Int): Int = {
    x * factor1
  }

}
