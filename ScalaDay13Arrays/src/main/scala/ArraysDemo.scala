object ArraysDemo {

  def main(args: Array[String]): Unit = {
    // Basic Array Declaration
    var arr:Array[Int] = new Array[Int](10)
    // Initialize the array
    for (i <- arr.indices) { // Can loop the Scala way (i is the index)
      arr(i) = i
    }
    for (i <- 0 until arr.length) { // Can loop the normal way (i is the index)
      println(s"${arr(i)}")
    }

    // Any array
    var arr2 = new Array[Any](3)
    arr2(0) = 10
    arr2(1) = "Hello There!"
    arr2(2) = 4.5
    for (i <- arr2.indices) {
      println(s"${arr2(i)}")
    }

    // Array with initializer list
    var arr3 = Array(10, 20, 30)
    for (i <- arr3) { // Can loop like this as well (makes i the actual value)
      println(s"${i}")
    }

    // foreach example
    var arr4 = Array(1,2,3,4,5,6,7,8,9,10)
    arr4.foreach(println) // foreach loop
    // Find the average
    var total = 0
    arr4.foreach(elem => total += elem) // The foreach way which is like javascript
    // shorthand notation arr4.foreach(total+=_)
    var average = total / arr4.length
    println(s"The average of the arr4 is ${average}")

    // map example
    var newArr = arr4.map(elem => elem + 10)
    newArr.foreach(println)

    // Array with yield
    var newArr2 = for (elem <- newArr) yield { elem + 10 }
    newArr2.foreach(println)

  }

}
