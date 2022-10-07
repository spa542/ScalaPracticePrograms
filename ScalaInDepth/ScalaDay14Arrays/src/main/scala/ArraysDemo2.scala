import scala.Array.ofDim // For multidimensional arrays
import scala.Array.range // For pythonic range function
import scala.collection.mutable.ArrayBuffer // For making a dynamic length array

object ArraysDemo2 {

  def main(args: Array[String]): Unit = {
    // More array Stuff
    var arr = Array(10, 12, 14, 16, 18, 20)
    var avg = arr.reduceLeft((x, y) => (x + y) / 2) // chains the results from left to right
    println(s"${avg}")
    // For further understanding
    var avg2 = arr.reduceLeft((x, y) => {
      println(s"Value of x is ${x} and value of y is ${y} and average is " + (x+y)/2)
      (x + y) / 2
    })
    println(s"Final average: ${avg2}")
    // Using this to get the total
    var total = arr.sum // using shorthand
    var max = arr.reduceRight(_ max _) // using shorthand (but there is already a function for this (arr.max)
    println(s"The total is ${total} and the max is ${max}")

    // Multi-dimensional Array
    var matrixExample = ofDim[Int](3, 3)
    for (row <- 0 to 2) {
      for (column <- 0 to 2) {
        matrixExample(row)(column)  = column + 3
      }
    }
    matrixExample.foreach(row => row.foreach(println)) // printing the 2d array

    // Range
    var rollNo = range(1, 100, 2) // Start, Stop, Step
    rollNo.foreach(elem => print(elem + " "))

    // You can also combine a bunch of arrays into another array to make a multidimensional array
    var arrComb1 = Array(1, 2, 3)
    var arrComb2 = Array(4, 5, 6)
    var arrComb3 = Array(7, 8, 9)
    var combArr = Array(arrComb1, arrComb2, arrComb3)
    combArr.foreach(arr => arr.foreach(println))

    // Create a changeable length array
    var dynamicArray = ArrayBuffer[Int]()
    dynamicArray += 10
    dynamicArray += 3
    dynamicArray += 2
    dynamicArray.foreach(elem => print(elem + " "))
    println
    dynamicArray.append(25)
    dynamicArray.foreach(elem => print(elem + " "))
    println
    dynamicArray.remove(2)
    dynamicArray.foreach(elem => print(elem + " "))
    println
  }

}
