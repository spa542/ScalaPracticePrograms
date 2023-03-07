package demo.part5TypeSystem

object RockingInheritance extends App {

  // Convenience
  trait Writer[T] {
    def write(value: T): Unit
  }
  trait Closeable {
    def close(status: Int): Unit
  }
  trait GenericStream[T] {
    // some methods
    def foreach(f: T => Unit): Unit
  }

  // This trait with 2 other traits is it's own type
  // Whenever we don't know if the parameter uses specific traits, we type all of the possible traits
  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // Diamond Problem

  trait Animal { def name: String }
  trait Lion extends Animal {
    override def name: String = "lion"
  }
  trait Tiger extends Animal {
    override def name: String = "tiger"
  }
  class Mutant extends Lion with Tiger { // this code compiles even if Lion and Tiger have overrides
    //override def name: String = "ALIEN"
  } // This code also compiles EVEN IF the override is removed (which name override do we call when calling name??)

  val m = new Mutant
  println(m.name) // tiger

  /*
    Mutant extends Animal with { override def name: String = "lion"}
    with Animal with { override def name: String = "tiger" } // Compiler uses this (last override gets picked!!)
   */

  // The Super Problem + Type Linearization

  trait Cold {
    def print = println("Cold")
  }

  trait Green extends Cold {
    override def print: Unit = {
      println("Green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("Blue")
      super.print
    }
  }

  class Red {
    def print = println("Red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("White")
      super.print
    }
  }

  val color = new White
  color.print // Prints White Blue Green Cold
  // Type Linearization for White (Calling super gets a new meaning, will take a look at the type immediately to the left of the hierarchy)
  // By the time it hits 4, the compiler has seen everything in the hierarchy, Red is overridden with trait hierarchy
  //          4        3          2           1
  // White = Red with Green with Blue with <White>
  // = AnyRef with <Red>
  //  with (AnyRef with <Cold> with <Green>)
  //  with (AnyRef with <Cold> with <Blue>)
  //  with <White>

}
