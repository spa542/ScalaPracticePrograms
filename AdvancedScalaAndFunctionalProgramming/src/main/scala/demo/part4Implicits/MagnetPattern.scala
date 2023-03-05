package demo.part4Implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  // Magnet Pattern is use case of type classes which aims at solving some of the problems of method overloading

  // Method Overloading

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    //def receive[T](message: T)(implicit serializer: Serializer[T]) // use context bound!
    def receive[T: Serializer](message: T): Int
    def receive[T: Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PResponse]): Int
    // def receive(future: Future[P2PResponse]): Int // Not compilable
    // lots of overloads...
  }

  /*
    1. Type Erasure
    2. Lifting doesn't work for all overloads

      val receiveFV = receive _ means what??

    3. Code duplication
    4. Type inference and default args

      actor.receive(?!) what is the default arg to be fetched?
   */

  // This API can be rewritten!
  // Type class and receive method act like a center of gravity

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  // Single receive method will be called and compiler will implicitly match the correct method to the magnet call
  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling a P2PRequest
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling a P2PResponse
      println("Handling P2P response")
      48
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse)

  // Benefits of Magnet Pattern
  // 1. No more type erasure problems!
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 3
  }

  println(receive(Future(new P2PRequest)))
  println(receive(Future(new P2PResponse)))

  // 2. Lifting
  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(s: String): Int = s.toInt + 1
    // add1 overloads
  }

  // "magnetize"
  trait AddMagnet { // no type parameter here (compiler would not know which type the addFV applies to)
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _ // lifting here
  println(addFV(1))
  println(addFV("3"))

  // val receiveFV = receive _ // will consider the receive as MessageMagnet[Nothing]

  // Drawbacks of Magnet Pattern
  // 1. Super verbose!
  // 2. Harder to Read
  // 3. You can't name or place default arguments
  //    receive() :(
  // 4. Call by name doesn't work correctly
  // (exercise) prove it! (side effects)

  class Handler {
    def handle(s: => String) = {
      println(s)
      println(s)
    }
    // other overloads
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala!")
    "hahaha"
  }

  //handle(sideEffectMethod())
  handle {
    println("Hello, Scala!")
    "hahaha" // new StringHandle("hahaha")
  }
  // This is super hard to trace...

}
