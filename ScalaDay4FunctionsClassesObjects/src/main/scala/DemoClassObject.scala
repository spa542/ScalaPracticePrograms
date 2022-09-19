class Car {
  private var topClassExtraCost = 0 // Can explicitly define private or protected, otherwise is public
  var roadTax = 100
  protected var prot = 10

  def cost(basicCost:Int): Int = {
    (basicCost + topClassExtraCost) * check_tax() // Can omit the return type and just do the calculation
  }

  private def check_tax(): Int = {
    this.roadTax + this.prot
  }
}


object DemoClassObject {

  def main(args: Array[String]): Unit = {
    println("Hello ABC")

    println("Creating the class instance")
    var car1 = new Car // Creating the object
    var result = car1.cost(10)
    println("The cost is " + result)
    println("Accessing the member variable roadTax: "+ car1.roadTax)
  }

}
