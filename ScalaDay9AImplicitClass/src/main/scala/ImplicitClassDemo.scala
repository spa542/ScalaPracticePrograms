object ImplicitClassDemo {

  // Impilicit Class for String Class
  // This cannot be used at the top level, must be inside of the object
  implicit class NewStringClass(s:String) {
    def firstChar() = s.substring(0,1)
  }

  def main(args: Array[String]): Unit = {
    val strr = "Hello World!"
    println(strr)
    // Regular toUpperCase function
    println(strr.toUpperCase())
    // New function created
    println(strr.firstChar())
  }

}
