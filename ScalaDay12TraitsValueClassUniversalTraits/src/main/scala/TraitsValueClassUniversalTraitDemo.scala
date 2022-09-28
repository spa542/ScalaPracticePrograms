// Works the same as in java
abstract class Vehicle {
  val x = 10
  def category() // Needs to be implemented
}


trait FourWheeler {
  def color(): Unit = {
    println("Color is orange")
  }
}


// Traits are basically just interfaces
trait Car {
  // Sample variable
  // Sample implemented function
  def engine(): Unit = {
    println("Engine is 1000CC")
  }

  // Need to be implemented
  def brake()
  def tires()
}

// You can force an extension of certain classes
// ex.
trait CarForce {
  this: Vehicle with FourWheeler => {

  }
}
// If this is used with any extend, it will force the class to also extend Vehicle and FourWheelers as well

// Value trait sample
trait ValueTrait extends Any {
  def print(): Unit = {
    println(this)
  }
}
// Value class sample
class MercedesValueClass(val x: Int) extends AnyVal with ValueTrait {
  // Cannot define any variables in a value class. Can only define methods
  def hello: Unit = { // Function definition is a little different (is called a little different
    println("Hello")
  }
}


class Mercedes extends Vehicle with FourWheeler with Car {
  // NOTE: Can reimplement engine if needed

  // Can override variables too (must be immutable variables to override
  override val x = 25

  // These functions need to be implemented because the function bodies are not present in the Car trait class
  def brake(): Unit = {
    println("Disk breaks")
  }
  def tires(): Unit = {
    println("Tires screech")
  }
  // Overriding a method that is already implemented in the trait
  override def color(): Unit = {
    super.color()
    println("The color is actually purple")
  }

  // MUST implement the abstract method
  override def category(): Unit = {
    println("This is from the abstract class!")
  }
}


object TraitsValueClassUniversalTraitDemo {

  def main(args: Array[String]): Unit = {
    var mercedes = new Mercedes()

    // Testing implementations
    mercedes.engine() // Implemented in trait
    mercedes.brake()
    mercedes.tires()
    mercedes.color() // From FourWheeler trait (which is also overridden)
    mercedes.category() // From the abstract class
    println(s"x variable overridden is ${mercedes.x}")
    var valueClass = new MercedesValueClass(10)
    valueClass.hello // It will be used as such
  }

}
