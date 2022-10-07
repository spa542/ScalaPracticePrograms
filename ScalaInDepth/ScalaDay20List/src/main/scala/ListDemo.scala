import scala.collection.mutable.ListBuffer

case class Car(val name:String, val cost:Int) {
  // Nothing here (for testing purpose)
}

object ListDemo {

  def main(args: Array[String]): Unit = {
    // More complex list operations
    var colors = List("Blue", "Purple", "Red", "Pink")
    val fillColors = List.fill(5)("Red")
    println(colors)
    println(fillColors)

    // Tabulate
    // For each index, do something...
    val numbers = List.tabulate(5)(i => i * 10)
    println(numbers)

    // More simple functions for list
    println(numbers.reverse)
    println(numbers.sorted(Ordering.Int.reverse))

    // Sorting methods
    println(numbers.sortBy(x => x)) // ascending order
    println(numbers.sortBy(x => x).reverse) // descending order
    println(numbers.sortBy(x => 10 / (x + 1))) // Sort in ascending by result of computation

    // Testing with classes (sortBy)
    val car1 = Car("Mercedes", 1000000)
    val car2 = Car("BMW", 500000)
    val car3 = Car("Jaguar", 70000)
    val listOfCars = List(car1, car2, car3)
    println(listOfCars)
    println(listOfCars.sortBy(car => car.cost))
    println(listOfCars.sortBy(car => car.name))

    // sortWith - takes two elements and then works on them in a bubble sort type of way
    println(numbers.sortWith((x, y) => x < y)) // ascending order
    println(numbers.sortWith((x, y) => x > y)) // descending order
    println(listOfCars.sortWith((c1, c2) => c1.cost > c2.cost)) // descending order by cost
    println(listOfCars.sortWith(sortingLogic))

    // Mutable listBuffer (Create an empty list buffer)
    val numbers2 = new ListBuffer[Int]()
    println(numbers2)
    numbers2 += 0; numbers2 += 1; numbers2 += 2
    println(numbers2)
    val numbersList = numbers2.toList
    println(numbersList)
  }

  // This will be passed to the sortWith function
  def sortingLogic(c1: Car, c2: Car): Boolean = {
    if (c1.cost > c2.cost) {
      true
    }
    false
  }

}
