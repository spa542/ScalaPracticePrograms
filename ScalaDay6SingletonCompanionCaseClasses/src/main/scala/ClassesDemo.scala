// This object gets referenced directly instead of being instantiated first
object SingletonDemoObject {

  var a = 10
  var b = 15

  def test(): Unit = {
    println("Hello from the singleton object!")
  }

}


// This is a companion class associated with a companion object
class Companion {
  var x = 5

  def getClassValue(): Unit = {
    println(s"The value of x is ${this.x}")
    println(s"The value of y from the object is ${Companion.y}") // This can be used directly from the object
  }
}
// This is a companion object associated with a companion class
object Companion {
  var y = 10
}


// Case Class Example
case class Car(name: String, model: String) {
  var carName = name
  var carModel = model

  def printDetails(): Unit = {
    println(s"Car name is ${this.carName} and Car model is ${this.carModel}")
  }

}


object ClassesDemo {

  def main(args: Array[String]): Unit = {
    // Singleton Object Test
    SingletonDemoObject.test() // No instantiation, just reference anything within the object (kind of like a struct in c++)
    println(s"The values from SingletonDemoObject are ${SingletonDemoObject.a} and ${SingletonDemoObject.b}")

    // Companion Class and Object Test
    val companionClass = new Companion
    println("This value is in the companion class "); companionClass.getClassValue()

    // Case Class and Object Test
    val bmw = Car("BMW", "550") // No need to write new keyword here, since apply method is auto-generated in background
    println("Printing the car details for the case class Car")
    bmw.printDetails()
    println("Changing the Car name")
    //bmw.name = "Hello" // Constructor parameter is val by default, therefore mutator method is not auto-generated and this parameter cannot be changed
    bmw.carName = "B.M.W"
    bmw.printDetails()
    bmw match {case Car(a, b) => println(a, b)} // Takes the first and second parameter and prints them from the Car class, case class auto-generates an apply method
    val mercedes = bmw.copy(name = "mercedes") // Can do a strict copy in this way
    println(s"Copied the bmw object to the mercedes object and changed the name attribute: "); mercedes.printDetails()
    // Equals and hashcode method
    println(bmw == mercedes)
    // toString method is autoimplemented
    println(bmw)
  }

}
