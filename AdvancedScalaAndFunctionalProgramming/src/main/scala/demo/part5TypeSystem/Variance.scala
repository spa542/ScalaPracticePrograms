package demo.part5TypeSystem

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // What is variance?
  // Variance is the problem of "inheritance" (type substitution) of generics

  class Cage[T]
  // Should a Cage[Cat] inherit from Cage[Animal?
  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
//  val icage: CCage[Animal] = new ICage[Cat] // This is wrong!
//  val x: Int = "hello world!"

  // hell no - opposite = contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal] // Why would we want this?
  // If the value on the right side can contain any animal, it can also contain a cat

  class InvariantCage[T](animal: T) // invariant

  // Covariance and Covariant Positions
  class CovariantCage[+T](val animal: T) // Generic vals of field animal are in a COVARIANT POSITION
  // Covariant positions also accept invariant types!

  //class ContravariantCage[-T](val animal: T)
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile) // This is not ok, filling a specific cage with a different type of animal
   */

  //class CovariantVariableCage[+T](var animal: T) // Types of vars are in CONTRAVARIANT POSITION
  /*
    val ccage: CCage[Animal] = new CCage[Cat](new Cat)
    ccage.animal = new Crocodile
   */
  //class ContravariantVariableCage[-T](var animal: T) // This will not compile, variable here is in COVARIANT POSITION
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */
  class InvariantVariableCage[T](var animal: T) // ok
  // covariant and contravariant positions are compiler restrictions

//  trait AnotherCovariantCage[+T] {
//    def addAnimal(animal: T) // method arguments are in CONTRAVARIANT POSITION
//  }
  /*
    val ccage: CCage[Animal] = new CCage[Dog]
    ccage.add(new Cat)
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true // this is fine
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  //acc.addAnimal(new Dog) // This cannot be done, so argument in method is ok to compiler
  class Kitty extends Cat
  acc.addAnimal(new Kitty) // inheritance is ok

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type (claiming B is a supertype of A)
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat) // Compiler is happy because Cat is a super type of Kitty, and will return a new MyList[Cat]
  // Will be a list of type Animal
  val evenMoreAnimals = moreAnimals.add(new Dog) // Dog is an Animal (will widen the return type of more animals to Animal (all elements in the list has the same type)

  // Method Arguments are in CONTRAVARIANT POSITION!!!!!!!

  // Method Return Types
//  class PetShop[-T] {
//    def get(isItAPuppy: Boolean): T // Method return types are in COVARIANT POSITION
//  }
  /*
    val catShop = new PetShop[Animal] {
      def get(isItAPuppy: Boolean): Animal = new Cat
    }

    val dogShop: PetShop[Dog] = catShop
    dogShop.get(true) // we would get a cat here!
   */

  // Way to fix this is use subtypes
  class PetShop[-T] {
    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal // ok
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  //val evilCat = shop.get(true, new Cat) // Cat does not extend Dog so this call is illegal!
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova) // ok

  /*
    Big Rule:
    - method arguments are in CONTRAVARIANT position
    - return types are in COVARIANT position
   */

  /*
    Exercises:
    1. Invariant, Covariant, and Contravariant
      Parking[T](things: List[T]) {
        park(vehicle: T)
        impound(vehicles: List[T])
        checkVehicles(conditions: String): List[T]
      }

     2. Think about how API would be different if we used someone else's API: IList[T]

     3. Parking = monad!
        - flatMap
   */
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  // 1

  // Invariant // Allow for one type and only one
  class InvariantParking[T](things: List[T]) {
    def park(vehicle: T): InvariantParking[T] = ???
    def impound(vehicles: List[T]): InvariantParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => InvariantParking[S]): InvariantParking[S] = ???
  }
  // Covariant
  class CovariantParking[+T](things: List[T]) {
    def park[B >: T](vehicle: B): CovariantParking[B] = ???
    def impound[B >: T](vehicles: List[B]): CovariantParking[B] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CovariantParking[S]): CovariantParking[S] = ???
  }
  // Contravariant
  class ContravariantParking[-T](things: List[T]) {
    def park(vehicle: T): ContravariantParking[T] = ???
    def impound(vehicles: List[T]): ContravariantParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    //                   Function1[T, ContravariantParking[S]]
    def flatMap[R <: T, S](f: R => ContravariantParking[S]): ContravariantParking[S] = ???
  }

  /*
    Rule of Thumb
      - use covariance - COLLECTION OF THINGS
      - use contravariance - GROUP OF ACTIONS YOU WANT TO PERFORM ON YOUR TYPES
   */


  // 2
  class IList[T]
  // Invariant would be same as above
  // Covariant
  class CovariantParking2[+T](things: IList[T]) {
    def park[B >: T](vehicle: B): CovariantParking2[B] = ???
    def impound[B >: T](vehicles: List[B]): CovariantParking2[B] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }
  // Contravariant
  class ContravariantParking2[-T](things: List[T]) {
    def park(vehicle: T): ContravariantParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): ContravariantParking2[S] = ??? // Add subtypes because invariant list is in contravariant position
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

  // 3
  // See above



}
