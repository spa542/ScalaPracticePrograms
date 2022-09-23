import scala.util.control.Breaks

object ConditionalsLoopsDemo {

  def main(args: Array[String]): Unit = {
    val x = 7
    // Basic if statement
    if (x > 5) { // uses all the same operators as java
      println("x is greater than 5")
    }

    val y = 4
    // if/else
    if (y > 5) {
      println("This wont happen")
    } else if (y == 5) {
      println("This wont happen either")
    } else {
      println("y is not greater than or equal to 5")
    }

    // Loops
    // while loop
    var z = 1
    while (z < 10) {
      println(s"while loop value of z is ${z}")
      z += 1
    }
    // do while loop
    z = 1
    do {
      println(s"do while loop value of z is ${z}")
      z += 1
    } while (z < 10)
    // for loop
    // to
    for (i <- 1 to 10) { // 1 to 10
      println(s"for loop 1 to value of i is ${i}")
    }
    // until
    for (i <- 1 until 10) { // 1 to 9
      println(s"for loop 2 until value of i is ${i}")
    }
    // nested
    for (i <- 1 to 10) { // Nested for loop works the same
      for (j <- 1 to 10) {
        println(s"value of i is ${i} and value of j is ${j}")
      }
    }
    // nested - Scala
    for (i <- 1 to 10; j <- 1 to 10) {
      println(s"scala nested value of i is ${i} and value of j is ${j}")
    }
    // arrays / list
    // List is similar to arrays - lists are immutable - like a tuple in python
    var numbersList = List(1,2,3,4,5,6,7,8,9,10)
    for (num <- numbersList) {
      println(s"Num in list is equal to ${num}")
    }
    // For loops for Collections with filter
    for (num <- numbersList if num % 2 == 0) { // Can add filter to loop
      println(s"Num in list is equal to ${num}")
    }
    // For loops for Collections with multiple filters
    for (num <- numbersList if num % 2 == 0; if num != 4) {
      println(s"Num in list is equal to ${num}")
    }
    // For loops for Collections with filter with yield
    var evenNumberList = for (i <- numbersList if i % 2 == 0) yield i
    println(evenNumberList)
    // break statement - scala 2.8 >=
    // For loops for Collections with filter and break
    val breakObject = new Breaks
    breakObject.breakable {
      for (i <- numbersList if i % 2 == 0) {
        println(s"Value of i is ${i}")
        if (i == 4) {
          println("I am breaking at 4")
          breakObject.break()
        }
      }
    }
  }

}
