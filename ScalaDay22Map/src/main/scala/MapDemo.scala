object MapDemo {

  def main(args: Array[String]): Unit = {
    // Basic Map Stuff
    var baseMap = Map("One"->"Value", "Hello"->"There", 1->"Yeet")
    println(baseMap)
    println(baseMap("One"))
    baseMap += ("Suzuki"->"Hello")
    println(baseMap)

    // Useful functions
    println(baseMap.isEmpty)
    println(baseMap.head)
    var mutableMap = scala.collection.mutable.Map("Yert"->5, "Naga"->"Hello",10->100)
    println(mutableMap)
    mutableMap.remove("Naga") // Remove one item from map
    println(mutableMap)
    mutableMap -= "Yert"
    println(mutableMap) // Remove another item from map but in a different way
    println(mutableMap.get(10)) // Get an element and will not break if key dne
    println(mutableMap(10)) // Get an element but will break if key dne
    // ++ works the same for map as it does for other objects
    println(baseMap.contains(1)) // checking for keys

    // Printing all foreach
    baseMap.foreach(mapItem => println(s"Key is ${mapItem._1} and value is ${mapItem._2}"))

    // valuesIterator
    baseMap.valuesIterator.foreach(println) // iterating over the values of the map

    // Creating map to get default values if the key is not found
    var defaultMap = scala.collection.mutable.Map("Hello"->"Woah", 10->12).withDefaultValue("Null")
    println(defaultMap("YEET")) // Should get default value here

    // There are many types of maps to mess around with (see documentation)
    // ListMap, LinkedHashMap, HashMap
  }

}
