package demo.part5TypeSystem

object StructuralTypes extends App {

  // Structural Types / Compile-Time Duck Typing

  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("yeah yeah I'm closing")
    def closeSilently(): Unit = println("Not making a sound!")
  }

  //def closeQuietly(closeale: JavaCloseable OR HipsterCloseable) // would be nice if we could do this!?

  type UnifiedCloseable = {
    def close(): Unit
  } // Structural Type!

  def closeQuietly(unifiedCloseable: UnifiedCloseable): Unit = unifiedCloseable.close()

  closeQuietly(new JavaCloseable {
    override def close(): Unit = println("JavaCloseable!")
  })

  closeQuietly(new HipsterCloseable {
    override def close(): Unit = println("HipsterCloseable!")
  })

  // Type Refinements

  type AdvancedCloseable = JavaCloseable {
    def closeSilently(): Unit // enriched!
  }

  class AdvancedJavaCloseable extends JavaCloseable {
    override def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently!!")
  }

  def closeShh(advCloseable: AdvancedCloseable): Unit = advCloseable.closeSilently()

  //closeShh(new AdvancedCloseable)
  //closeShh(new HipsterCloseable) // not ok because it does not originate from JavaCloseable despite having the same methods

  // Using Structural Types as Standalone Types
  def altClose(closeable: { def close(): Unit }): Unit = closeable.close()

  // type-checking => duck-typing

  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark!")
  }

  class Car {
    def makeSound(): Unit = println("vroom!")
  }

  val dog: SoundMaker = new Dog // defined the same so this works
  val car: SoundMaker = new Car // Types on the right side must match the types on the left side

  // static duck typing (present in dynamic typing) (can do this duck typing test at compile time!)

  // CAVEAT: based on reflection (have a big impact on performance) (use only when needed!)

  /*
    Exercises
   */
  // 1.
  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }

  class Brain {
    override def toString: String = "BRAINZ!"
  }

  def f[T](somethingWithAHead: { def head: T }): Unit = println(somethingWithAHead.head)

  /*
    f is compatible with CBL and with a Human? Compatible with both!
   */

  case object CBNil extends CBL[Nothing] {
    def head: Nothing = ???
    def tail: CBL[Nothing] = ???
  }

  case class CBCons[T](override val head: T, override val tail: CBL[T]) extends CBL[T]

  f(CBCons(2, CBNil))
  f(new Human) // ?! T = ? = Brain !!

  // 2.
  object HeadEqualizer {
    type Headable[T] = { def head: T }
    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  /*
    f is compatible with CBL and with a Human? Compatible with both!
   */
  val brainzList = CBCons(new Brain, CBNil)
  val stringsList = CBCons("Brainz", CBNil)
  HeadEqualizer.===(brainzList, new Human)
  // Problem:
  HeadEqualizer.===(new Human, stringsList) // this is wrong, not type safe!

  // Be very carefule with structural types when using them in the context of methods with type parameters!


}
