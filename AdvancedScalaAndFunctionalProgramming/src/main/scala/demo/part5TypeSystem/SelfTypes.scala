package demo.part5TypeSystem

object SelfTypes extends App {

  // Requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  // This is known as a self type
  // can use any keyword instead of "self"
  // We want singer and instrumentalist to be in two different contexts
  trait Singer { self: Instrumentalist => // whoever implements singer to implement Instrumentalist as well
    def sing(): Unit
  }

  // This is valid
  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = println("play 1")
    override def sing(): Unit = println("sing 1")
  }

//  class Vocalist extends Singer {
//    override def sing(): Unit = ???
//  }

  val jamesHetfield = new Singer with Instrumentalist {
    override def play(): Unit = println("play 2")
    override def sing(): Unit = println("sing 2")
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("Guitar solo")
  }

  // This is also fine
  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = println("sing 3")
  }

  // Self types are often compared to inheritance
  class A
  class B extends A // B is A

  trait T
  trait S { self: T => } // S is not a T but REQUIRES a T

  // Cake Pattern => "dependency injection"

  // Classical DI
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  // Cake Pattern Example
  trait ScalaComponent {
    // API
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks!" // this works because whoever implements ScalaDependentComponent must also implement ScalaComponent
  }
  trait ScalaApplication { self: ScalaDependentComponent => }

  // At each layer, can choose what components from the previous layer to "inject"
  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose components
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app components
  trait AnalyticsApp extends ScalaApplication with Analytics


  // Cyclical Dependencies (this is possible with self types)
//  class X extends Y
//  class Y extends X

  // Need to watch for this
  trait X { self: Y => }
  trait Y { self: X => }

}
