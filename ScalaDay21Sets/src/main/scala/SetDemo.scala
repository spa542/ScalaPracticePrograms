import scala.collection.{SortedSet, mutable}

object SetDemo {

  def main(args: Array[String]): Unit = {
    // Sample Set Examples
    var baseSet = Set[Int]()
    var setWithElements = Set("India", "US", "Mexico")
    println(baseSet)
    println(setWithElements)
    println(setWithElements.getClass)

    // Mutable Set
    var mutableSet = scala.collection.mutable.Set("Hello", "Hi", "Other")
    mutableSet.add("Woah") // Add to the set
    mutableSet += "Added"
    println(mutableSet)

    // Useful Functions
    println(mutableSet.head)
    println(mutableSet.isEmpty)
    var greaterSet = setWithElements ++ mutableSet // Combining two sets
    println(greaterSet)
    println(mutableSet.min)
    mutableSet.remove("Woah")
    println(mutableSet)
    setWithElements += ("Hi", "There") // Adding two elements to mutable set
    println(setWithElements)
    setWithElements -= "There" // Remove the element
    println(setWithElements)

    // Converts Set into Array/List
    var newList = setWithElements.toList
    println(newList)

    // Sorted Set
    var sortedSet = SortedSet("I", "am" , "sorted")
    println(sortedSet)

    // LinkedHashSet
    var linkHash = mutable.LinkedHashSet("Oof", "Yeet")
    println(linkHash)

    // Queue
    var newQueue = mutable.Queue(1, 5, 7, 8, 3)
    newQueue.dequeueAll(i => i % 2 == 0) // Dequeue all even numbers
    println(newQueue)
  }

}
