package demo.part4Implicits

import java.{util => ju}

object ScalaJavaConversions extends App {

  import collection.JavaConverters._

  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)

  val scalaSet = javaSet.asScala

  /*
    Iterator
    Iterable
    ju.List - scala.mutable.Buffer
    ju.Set - scala.mutable.Set
    ju.Map - scala.mutable.Map
   */

  // There is also Scala to Java conversions for mutable containers

  import collection.mutable._
  val numbersBuffer = ArrayBuffer[Int](1,2,3)
  val juNumbersBuffer = numbersBuffer.asJava // through implicit wrapping

  println(juNumbersBuffer.asScala eq numbersBuffer) // References are equal! (some conversions cannot give back the same Java object!!!)

  val numbers = List(1,2,3)
  val juNumbers = numbers.asJava
  val backToScala = juNumbers.asScala
  println(backToScala eq numbers) // false because this is not referentially correct
  println(backToScala == numbers) // true (they are the same collection)

  //juNumbers.add(7) // acceptable but will throw because juNumbers is supposed to be immutable!

  /*
    Exercise: Implement scala to java conversions with Optional-Option
      .asScala
   */
  class ToScala[T](value: => T) {
    def asScala: T = value
  }

  implicit def asScalaOptional[T](o: ju.Optional[T]): ToScala[Option[T]] = new ToScala[Option[T]](
    if (o.isPresent) Some(o.get) else None
  )

  val juOptional: ju.Optional[Int] = ju.Optional.of(2)
  val scalaOption = juOptional.asScala // will force the compiler to wrap juOptional as an asScalaOptional[T]
  println(scalaOption)

}
