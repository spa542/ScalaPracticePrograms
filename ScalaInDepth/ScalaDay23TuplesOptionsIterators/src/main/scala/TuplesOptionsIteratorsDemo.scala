object TuplesOptionsIteratorsDemo {

  def main(args: Array[String]): Unit = {
    // Tuples
    var baseTuple = (1, "Hello There", 10.5)
    println(baseTuple)
    println(baseTuple.getClass)
    println(baseTuple._2)
    baseTuple.productIterator.foreach(println)
    println(baseTuple.toString())
    //println(baseTuple.swap) // Can only be done on a tuple with 2 elements
    var listOfTuples = List((1, "Hello"), (10.5, 10), ("Yeet", true))
    println(listOfTuples)
    listOfTuples.foreach(tuple => tuple.productIterator.foreach(println)) // Another way to print all elements
    // Normally use case class instead of tuple

    // Options
    val option1 = Some(10)
    val option2 = None
    println(option1)
    println(option1.isEmpty)
    println(option2)
    println(option2.isEmpty)

    // Iterators
    var iterator1 = Iterator("hello", "world", "why", "yes")
    println(iterator1.hasNext)
    println(iterator1.next())
    println(iterator1.hasNext)
    println(iterator1.next())
    println(iterator1.length)
    var bufferedIterator = iterator1.buffered
    println(bufferedIterator)
    println(bufferedIterator.head)
  }

}
