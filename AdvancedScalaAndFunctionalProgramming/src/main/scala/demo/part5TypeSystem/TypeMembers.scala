package demo.part5TypeSystem

object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  // Wont see too much in practice
  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal // must extend animal
    type SuperBoundedAnimal >: Dog // lower bounded in Dog but upper bounded in Animal
    type AnimalC = Cat // Another name for an existing type
  }

  val ac = new AnimalCollection
  //val dog: ac.AnimalType = ??? // cant do a construction cause there is no constructor

  //val cat: ac.BoundedAnimal = new Cat // cannot associate cat because we do not know what BoundedAnimal is

  val pup: ac.SuperBoundedAnimal = new Dog // Allows because this is some supertype of Dog (will not work for other types)
  val cat: ac.AnimalC = new Cat // This is fine! (use for type aliasing)

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat // This is fine!

  // An alternative to generics
  trait MyList {
    // Would need to override both of these members since they are both abstract!!!
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int
    def add(element: Int): MyList = ???
  }

  // .type
  type CatsType = cat.type // Can associate other things to this new type
  val newCat: CatsType = cat // Can only do association, cannot create new instances of this type
  //new CatsType

  /*
    Exercise - enforce a type to be applicable to SOME TYPES only
   */
  // LOCKED
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicaleToNumbers {
    type A <: Number
  }
  // Not OK
//  class CustomList(hd: String, tl: CustomList) extends MList with ApplicaleToNumbers {
//    type A = String // We dont want this to compile
//    def head = hd
//    def tail = tl
//  }
  // OK
  class CustomList(hd: String, tl: CustomList) extends MList {
    type A = String // We dont want this to compile
    def head = hd
    def tail = tl
  }

  // Number type
  // type members and type member constraints (bounds)

}
