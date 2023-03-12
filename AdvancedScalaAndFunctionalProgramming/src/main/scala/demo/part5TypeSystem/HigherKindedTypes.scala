package demo.part5TypeSystem

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object HigherKindedTypes extends App {

  // HKT - A deeper generic type with some unknown type parameter at the deepest level

  trait AHigherKindedType[F[_]]

  trait MyList[T] {
    def flatMap[B](f: T => B): MyList[B]
  }

  trait MyOption[T] {
    def flatMap[B](f: T => B): MyOption[B]
  }

  trait MyFuture[T] {
    def flatMap[B](f: T => B): MyFuture[B]
  }

  // Combine/Multiply List(1,2) x List("a", "b") => List(1a, 1b, 2a, 2b)

//  def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
//    for {
//      a <- listA
//      b <- listB
//    } yield (a, b)
//
//  def multiply[A, B](listA: Option[A], listB: Option[B]): Option[(A, B)] =
//    for {
//      a <- listA
//      b <- listB
//    } yield (a, b)
//
//  def multiply[A, B](listA: Future[A], listB: Future[B]): Future[(A, B)] =
//    for {
//      a <- listA
//      b <- listB
//    } yield (a, b)

  // Create a common API for all of these types even those these types are COMPLETELY unrelated
  // Use HKT

  trait Monad[F[_], A] { // Higher-Kinded Type Class
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  // Aiming to define for Monads in general (need to define map and flatMap)
  implicit class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  implicit class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }

  // F => List
  def multiply[F[_], A, B](implicit ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)
  /*
    ma.flatMap(a => mb.map(b => (a,b)))
   */

  val monadList = new MonadList(List(1,2,3))
  monadList.flatMap(x => List(x, x + 1)) // List[Int]
  // Monad[List, Int] => List[Int]
  monadList.map(_ * 2) // List[Int]
  // Monad[List, Int] = List[Int]

//  println(multiply(new MonadList(List(1,2)), new MonadList(List("a", "b"))))
//  println(multiply(new MonadOption[Int](Some(2)), new MonadOption[String](Some("Scala"))))
  println(multiply(List(1,2), List("a", "b")))
  println(multiply(Some(2), Some("Scala")))


}
