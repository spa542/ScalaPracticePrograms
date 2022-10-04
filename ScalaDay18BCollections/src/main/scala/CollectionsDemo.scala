import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.List

object CollectionsDemo {

  def main(args: Array[String]): Unit = {
    // We can change this because it is mutable and because it is a var
    var x = ArrayBuffer[Int](2,4,6,8,10)
    println(x)
    x.append(20)
    println(x)
    x = x.map(i => i + 1)
    println(x)

    // We cannot change this because it is immutable
    var y = List[Int](1,2,3,4,5,6,7,8,9,10)
    println(y)
    //y = y.map(i => i + 1)
    y = y:+30 // Overwriting the entire variable, not actually changing the list itself
    println(y)

  }

}
