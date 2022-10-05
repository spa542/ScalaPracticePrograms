object ListDemo {

  def main(args: Array[String]): Unit = {
    // First example
    val colors: List[String] = List("Red", "Green", "Yellow")
    val evenNumbers = List(2, 4, 6, 8, 10)
    val matrix = List(
      List(1, 2, 3),
      List(4, 5, 6),
      List(7, 8, 9)
    )
    println(colors)
    println(evenNumbers)
    println(matrix)

    // Another way of writing a list
    val matrix2 = ((1::(2::(3::Nil))) :: (4::(5::(6::Nil))) :: Nil)
    println(matrix2)

    // Joining two lists using ::
    val matrix3 = matrix :: matrix2
    println(matrix3)

    // Defining a list with a range
    val numbers = List.range(1, 101)
    println(numbers)
    val evenNumbers2 = List.range(2, 100, 2)
    println(evenNumbers2)
    val oddNumbers = (1 to 100 by 2).toList
    println(oddNumbers)

    // mkString
    // Will give a comma delimited string list of colors from colors list prefix (optional), delimiter, suffix (optional)
    val availableColors = colors.mkString("We are having these colors: ", ",", " - All colors are available right now")
    println(availableColors)

    // Simple Operations on List
    println(colors.head)
    println(colors.tail)
    println(colors.isEmpty)
    println(colors.size)
    println(colors.distinct)
    println(colors(1))

    // Adding an element to a list
    // Cannot do because it is a val
    // colors = colors += "Blue"
    var mutableColors = List("Purple", "Green", "Orange")
    mutableColors = mutableColors :+ "Pink"
    println(mutableColors)

    // Using foreach and other iterator loops
    // Traditional
    for (color <- mutableColors) {
      println(color)
    }
    // foreach
    mutableColors.foreach(println)
    // map
    mutableColors.map(println(_)) // mutableColors.map(color => println(color))

    // Using filter
    var isGreenContained = mutableColors.filter(color => color == "Green")
    println(isGreenContained)
    var isGreenContained2 = mutableColors.exists(color => color == "Green")
    println(isGreenContained2)
    var isGreenContained3 = mutableColors.contains("Green")
    println(isGreenContained3)
  }

}
