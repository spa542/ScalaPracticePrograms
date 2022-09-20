// Constructor - Can pass the arguments to the class like a function
class SampleClass(a:Int, b:Double, c:String) {
  val x = a
  val y = b
  val z = c

  def addNumber(): Double = {
    x + y
  }

  println(s"x = ${x} and y = ${y} and z = ${z}") // This is the string interpolation way of printing
  println("x = "+ this.x + " and y = "+ this.y +" and z = "+ this.z) // This is the Java way of printing
}


class SampleClass2(a:Int = 50, b:Double = 2.5, c:String = "Hello World") {
  val x = a
  val y = b
  private val z = c

  def addNumber(): Double = {
    x + y
  }

  println(s"x = ${x} and y = ${y} and z = ${z}") // This is the string interpolation way of printing
  println("x = "+ this.x + " and y = "+ this.y + " and z = "+ this.z) // This is the Java way of printing
}


// This class will use an auxiliary constructor
class SampleClass3(a:Int, b:Double, c:String) {

  val x = a
  val y = b
  val z = c

  def this(a:Int) {
    this(a, 90.9, "Hello auxiliary!") // Call the primary constructor here
    println("Auxiliary function called here!") // Auxiliary constructor called with 1 parameter here
  }

  def addNumber(): Double = {
    x + y
  }

  println(s"x = ${x} and y = ${y} and z = ${z}") // This is the string interpolation way of printing
  println("x = "+ this.x + " and y = "+ this.y + " and z = "+ this.z) // This is the Java way of printing

}

object Constructors {

  def main(args: Array[String]): Unit = {
    val sampleObject1 = new SampleClass(10, 5.89, "Hello There Bud!")

    println(s"The x and y values added equals ${sampleObject1.addNumber()}")

    val sampleObject2 = new SampleClass2() // Calling the class using default values

    println(s"The x and y values added using default values equals ${sampleObject2.addNumber()}")

    val sampleObject3 = new SampleClass3(5) // Calling the class using the auxiliary constructor

    println(s"The x and y values added using default values equals ${sampleObject3.addNumber()}")
  }

}
