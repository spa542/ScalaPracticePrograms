package demo.part5TypeSystem

object PathDependentTypes extends App {

  // You can define classes and objects ANYWHERE
  // Nested Types and how they are used and accessed
  class Outer {
    class Inner
    object InnerObject
    type InnerType // only exception cause these have to be defined within a type

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i) // Now the inner type can now be used for all inner instances of Inner
  }

  def aMethod: Int = {
    class HelperClas
    // some code...
    2
  }

  // Types nested inside classes/objects (Defined PER INSTANCE!)
  val o = new Outer
//  val inner = new Inner // Does not work
//  val inner = new Outer.Inner // Does not work
  val inner = new o.Inner // o.Inner is a TYPE

  val oo = new Outer
  //val otherInner: oo.Inner = new o.Inner // Does not work (different types!)

  o.print(inner)
  // oo.print(inner) // different types!

  // Path-Dependent Types

  // Outer#Inner (Can use for both here!)
  o.printGeneral(inner)
  oo.printGeneral(inner)

  /*
    Exercise:
    DB keyed by Int or String, but maybe others
   */

  /*
    use path-dependent types
    and abstract type members
    and/or type aliases
   */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }

  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42) // ok
  get[StringItem]("Home") // ok

  // get[IntItem]("Scala") // not ok

}
