package demo.part5TypeSystem

object Reflection extends App {

  // How do I instantiate a class or invoke a method by calling its name dynamically at runtime?
  // Reflection + Macros + Quasiquotes (Scala Reflection API Exclusively**) => Metaprogramming

  case class Person(name: String) {
    def sayMyName(): Unit = println(s"Hi, my name is $name")
  }

  // Step 0 - import
  import scala.reflect.runtime.{universe => ru}

  // Step 1 - Mirror
  val m = ru.runtimeMirror(getClass.getClassLoader)

  // Step 2 - Create a Class Object
  val clazz = m.staticClass("demo.part5TypeSystem.Reflection.Person") // Creating a class object by Name (this is a class symbol / description of the class)

  // Step 3 - Create a Reflected Mirror
  val cm = m.reflectClass(clazz) // can access it's members and do things as normal

  // Step 4 - Get the Constructor
  val constructor = clazz.primaryConstructor.asMethod // we want to invoke this here

  // Step 5 - Reflect the Constructor
  val constructorMirror = cm.reflectConstructor(constructor)

  // Step 6 - Invoke the Constructor
  val instance = constructorMirror.apply("Ryan")

  println(instance)

  // I have an instance already computed
  val p = Person("Mary") // from the wire as a serialized object
  // method name computed from somewhere else
  val methodName = "sayMyName" // can only run this dynamically
  // 1 - mirror
  // 2 - reflect the instance
  val reflected = m.reflect(p)
  // 3 - method symbol
  val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method = can do things!
  val method = reflected.reflectMethod(methodSymbol)
  // 5 - invoke the method
  method.apply()

  // Type-Erasure

  // Pain Point #1: You cannot differentiate between generic types at runtime
  val numbers = List(1,2,3)
  numbers match { // Types are erased at runtime
    case listOfStrings: List[String] => println("list of strings") // will get reduced to List
    case listOfInts: List[Int] => println("list of numbers") // will get reduced to List
  }

  // Pain Point #2: Limitations on Overloads
  // We can't do this
//  def processList(list: List[Int]): Int = 42 // will get reduced to List
//  def processList(list: List[String]): Int = 45 // will get reduced to List

  // TypeTags

  // 0 - import
  import ru._

  // 1 - creating a type tag manually
  val ttag = typeTag[Person]
  println(ttag.tpe) // FQ name of this tag

  class MyMap[K, V]

  // 2 - pass type tags as implicit parameters
  def getTypeArguments[T](value: T)(implicit typeTag: TypeTag[T]) = typeTag.tpe match {
    case TypeRef(_, _, typeArguments) => typeArguments
    case _ => List()
  }

  val myMap = new MyMap[Int, String]
  val typeArgs = getTypeArguments(myMap) // (typeTag: TypeTage[MyMap[Int, String]]
  println(typeArgs)

  // Creates the type tages before at compile time before the types are erased, and then continues at runtime
  // Make doe without the erased types
  def isSubtype[A, B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean = {
    ttagA.tpe <:< ttagB.tpe
  }

  class Animal
  class Dog extends Animal
  println(isSubtype[Dog, Animal])


  // I have an instance already computed
  // 3 - method symbol
  val anotherMethodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method = can do things!
  val sameMethod = reflected.reflectMethod(anotherMethodSymbol)
  // 5 - invoke the method
  sameMethod.apply()

}
