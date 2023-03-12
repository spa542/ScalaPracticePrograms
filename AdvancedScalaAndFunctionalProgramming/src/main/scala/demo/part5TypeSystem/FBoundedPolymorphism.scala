package demo.part5TypeSystem

object FBoundedPolymorphism extends App {

//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Animal] = ??? // List[Cat] !!
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Animal] = ??? // List[Dog] !!
//  }

  // Solution 1: Naive
//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Cat] = ??? // List[Cat] !!
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Cat] = ??? // List[Dog] !! // Allows this but this is still wrong!
//  }


  // Solution 2: FBP
//  trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ??? // List[Cat] !!
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!
//  }
//
//  trait Entity[E <: Entity[E]] // ORM
//  class Person extends Comparable[Person] { // FBP
//    override def compareTo(o: Person): Int = ???
//  }
//
//  class Crocodile extends Animal[Dog] { // Compilable but wrong!
//    override def breed: List[Animal[Dog]] = ???
//  }


  // Solution 3: FBP + Self Types
//  trait Animal[A <: Animal[A]] { self: A =>
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ??? // List[Cat] !!
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!
//  }
//
////  class Crocodile extends Animal[Dog] { // Will not compile here
////    override def breed: List[Animal[Dog]] = ???
////  }
//
//  trait Fish extends Animal[Fish]
//  class Shark extends Fish {
//    override def breed: List[Animal[Fish]] = List(new Cod) // wrong FBP has reached it's implementation
//  }
//
//  class Cod extends Fish {
//    override def breed: List[Animal[Fish]] = ???
//  }

  // Exercise

  // Solution 4: Type Classes

//  trait Animal
//  trait CanBreed[A] {
//    def breed(a: A): List[A]
//  }
//
//  class Dog extends Animal
//  // Notice that we have completely split the breed method away from Animal and Dog
//  object Dog {
//    implicit object DogsCanBreed extends CanBreed[Dog] {
//      override def breed(a: Dog): List[Dog] = List()
//    }
//  }
//
//  implicit class CanBreedOps[A](animal: A) {
//    def breed(implicit canBreed: CanBreed[A]): List[A] = canBreed.breed(animal)
//  }
//
//  val dog = new Dog
//  dog.breed // This is guaranteed to return a List[Dog]!!!
//  /*
//    new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)
//    implicit value to pass to breed: Dog.DogsCanBreed
//   */
//
//  // How does the compiler signal mistakes to me?
//  class Cat extends Animal
//  object Cat {
//    implicit object CatsCanBreed extends CanBreed[Dog] {
//      override def breed(a: Dog): List[Dog] = List()
//    }
//  }
//
////  val cat = new Cat
////  cat.breed // Canot lie about our types to the compiler!!

  // Solution 5: Pure Type Classes (Keeping the API inside the concept that we want to represent)

  // Lets let the trait Animal be the type class itself to simplify!
  trait Animal[A] { // Pure Type Classes
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  class Cat
  object Cat {
    implicit object CatAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] = animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed

//  val cat = new Cat
//  cat.breed // Compiler will catch this!

}
