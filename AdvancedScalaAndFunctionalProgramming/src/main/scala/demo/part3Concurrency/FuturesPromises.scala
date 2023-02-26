package demo.part3Concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration._ // for 2.seconds
// Important for Futures
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesPromises extends App {

  def calculateTheMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  // Compiler needs a value for the execution context to inject (IMPLICITS)
  val aFuture = Future {
    calculateTheMeaningOfLife // calculates the meaning of life on another thread
  } // (global) which is passed by the compiler

  println(aFuture.value) // Option[Try[Int]]

  println("Waiting on the future")
  // onComplete used for side effects
  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
    case Failure(e) => println(s"I have failed with $e")
  } // SOME thread (we cannot make any assumptions!)
  Thread.sleep(3000)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) = println(s"${this.name} poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )

    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      // fetching from the database
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  // client: mark to poke bill
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
//  mark.onComplete {
//    case Success(markProfile) => {
//      val bill = SocialNetwork.fetchBestFriend(markProfile)
//      bill.onComplete {
//        case Success(billProfile) => markProfile.poke(billProfile)
//        case Failure(e) => e.printStackTrace()
//      }
//    }
//    case Failure(e) => e.printStackTrace()
//  }
//  Thread.sleep(2000)

  // Functional Composition of Futures
  // Use these whenever we get the chance!!!!
  // map, flatMap, filter
  val nameOnTheWall = mark.map(profile => profile.name) // Future[Profile] => Future[String] (if fail, will fail with same exception)

  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile)) // Future[Profile] -> Future[Profile]

  val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z")) // Future[Profile] filtered

  // for-comprehensions
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(2000)

  // Fallbacks
  // In case the future fails with an exception inside, we can still return a dummy profile
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
    case e: Throwable => Profile("fb.id.0-dummy", "Forever Alone")
  }
  // what if we want to fetch a different profile?
  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
    // Return a Future with arguments that we should know "for sure" will return something we want
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }

  // If original Future succeeds, then it's value will be used here, otherwise, the argument will be run and if it succeeds,
  // that value will be used, otherwise the exception of the FIRST arg will be in the Future
  val fallbackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  // We may need to block on a future (secure transactions)
  // Online Banking App
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Ryan App Banking"

    def fetchUser(name: String): Future[User] = Future {
      // simulate a long computation or fetching from the database
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] =  Future {
      // simulate some process
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from the db
      // create a transaction
      // WAIT for the transaction to finish
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      // Use this only when you HAVE to block on a Future
      // Will throw an exception timeout if duration is too long
      Await.result(transactionStatusFuture, 2.seconds) // implicit conversions -> pimp my library
    }
  }

  println(BankingApp.purchase("Ryan", "iPhone 12", "big store", 3000))
  // In a sense, Futures are read only when they are done, but sometimes we need to complete a Future or set it at the
  // point of our choosing, therefore, promises

  // Promises
  val promise = Promise[Int]() // "controller" over a Future
  val future = promise.future

  // Thread 1 - "consumer"
  future.onComplete {
    case Success(r) => println("[consumer] I've received the value " + r)
  }

  // Thread 2 - "producer"
  val producer = new Thread(() => {
    println("[producer] crunching numbers...")
    Thread.sleep(1000)
    // "fulfilling" the promise
    // manipulates the internal future to complete with a successful value (42), which is then handled by onComplete by some consumer
    promise.success(42)
    // promise.failure(new Exception)
    println("[producer] done")
  })

  producer.start()
  Thread.sleep(1000)

  // Future / Promise paradigm separates the concern of handling future adn writing to a promise while eliminating concurrency issues almost completely

  /*
  1) Fulfill a Future IMMEDIATELY with a value
  2) inSequence(futureA, futureB) (run future b after it has made sure that future a has completed)
  3) first(futureA, futureB) => new Future (value of a if it finishes first, or value of b if it finishes first)
  4) last(futureA, futureB) => new Future with last value returned
  5) retryUntil(action: () => Future[T], condition: T => Boolean): Future[T]
   */

  // 1)
  def fulfillImmediately[T](value: T): Future[T] = Future(value)

  // 2)
  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] = first.flatMap(_ => second)

  // 3)
  def first[A](futureA: Future[A], futureB: Future[A]): Future[A] = {
    val promise = Promise[A]

    // Dont need this, use try complete!
//    def tryComplete(promise: Promise[A], result: Try[A]) = result match {
//      case Success(result) => try {
//        promise.success(result)
//      } catch {
//        case _ =>
//      }
//      case Failure(t) => try {
//        promise.failure(t)
//      } catch {
//        case _ =>
//      }
//    }

    // Can use tryComplete on the promise
    futureA.onComplete(promise.tryComplete)
    futureB.onComplete(promise.tryComplete)
    // Using auxilliary method
//    futureA.onComplete(tryComplete(promise, _))
//    futureB.onComplete(result => tryComplete(promise, result))

    promise.future
  }

  // 4)
  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // 1 promise which both futures will try to complete
    // 2nd promise which the LAST future will complete
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    // Using the bothPromise to take a True and False (True when first completes, pass, False whens second completes, execute)
    val checkAndComplete = (result: Try[A]) => if (!bothPromise.tryComplete(result)) lastPromise.complete(result)

    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)

    lastPromise.future
  }
  val fast = Future {
    Thread.sleep(100)
    42
  }
  val slow = Future {
    Thread.sleep(200)
    45
  }
  first(fast, slow).foreach(f => println("FIRST: " + f))
  last(fast, slow).foreach(l => println("LAST: " + l))

  Thread.sleep(1000)

  // 5)
  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] = {
    action().filter(condition).recoverWith {
      case _ => retryUntil(action, condition)
    }
  }

  val random = new Random()
  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("generated " + nextValue)
    nextValue
  }

  retryUntil(action, (x: Int) => x < 10).foreach(result => println("settled at " + result))
  Thread.sleep(10000)

  // Futures are immutable, "read-only" objects***
  // Promises are "writable-once" containers over a future***

}
