package demo.part3Concurrency

import java.util.concurrent.Executors

object Intro extends App {

  /*
    interface Runnable {
      public void run()
    }
   */
  // JVM Threads
  val runnable = new Runnable {
    override def run(): Unit = println("Running!!!")
  }
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  // Create a JVM thread => OS Thread
  aThread.start() // gives the signal to the JVM to start a JVM thread
  // runnable.run() // Doesnt do anything, needs to be in a thread object
  aThread.join() // blocks until a thread finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodbye.start()
  // different runs produce different results!

  // Executors
  // Used to create batches of reusable threads, (threads are expensive to create and run in JVM!)
  // Dont need to start and stop threads
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))


//  pool.execute(() => {
//    Thread.sleep(1000)
//    println("done after 1 second")
//  })
//
//  pool.execute(() => {
//    Thread.sleep(1000)
//    println("almost done")
//    Thread.sleep(1000)
//    println("done after 2 seconds")
//  })

  // No more actions can be submitted
  pool.shutdown()

  // Will throw an exception in the calling thread (after shutdown)
  // pool.execute(() => println("should not appear"))

  // Interrupts the threads in the pool even if they are still mid-action and shuts them down
  // pool.shutdownNow()
  println(pool.isShutdown) // true after shutdown

  // Classic Race Conditions
  def runInParallel = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)
  }

  //for (_ <- 1 to 100) runInParallel

  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price // account.amount = account.amount - price
//    println("I've bought " + thing)
//    println("My account is now " + account)
  }

  // Classic Race Condition
//  for (_ <- 1 to 100) {
//    val account = new BankAccount(50000)
//    val thread1 = new Thread(() => buy(account, "shoes", 3000))
//    val thread2 = new Thread(() => buy(account, "iphone 12", 4000))
//
//    thread1.start()
//    thread2.start()
//    Thread.sleep(100)
//    if (account.amount != 43000) println("AHA: " + account.amount)
//    //println()
//  }

  // Fixes
  // Option 1: use synchronized()
  // Much more powerful option!!!
  def buySafe(account: BankAccount, thing: String, price: Int) =
    account.synchronized {
      // no two threads can evaluate this at the same time
      account.amount -= price
      println("I've bought " + thing)
      println("My account is now " + account)
    }

  // Option 2: use @volatile
  class VolatileBankAccount(@volatile var amount: Int) {
    override def toString: String = "" + amount
  }

  /*
    Exercises

    1) Construct 50 "inception" threads
      thread1 -> thread2 -> thread 3
      println("hello from thread #3")
      in reverse order
   */
  def inception(counter: Int, maxThreads: Int): Unit = {
    if (counter == maxThreads) {
      return
    }
    val tempThread = new Thread(() => inception(counter + 1, maxThreads))
    tempThread.start()
    tempThread.join()
    println("hello from thread " + counter)
  }
  val inceptionThreads = new Thread(() => inception(0, 50))
  inceptionThreads.start()

  // 2)
  var x = 0
  val threads = (1 to 100).map(_ => new Thread(() => x += 1))
  threads.foreach(_.start())
  /*
    1) what is the biggest value possible for x?
    100
    2) what is the smallest value possible for x?
    1
   */

  // Sleep Fallacy
  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is awesome!"
  })

  message = "Scala sucks!"
  awesomeThread.start()
  Thread.sleep(2000)
  //awesomeThread.join() // need to wait to fix issue here
  println(message)
  // What's the value for message?
  // Could be either first or second
  // Is it guaranteed?
  // Not guaranteed
  // Why or why not?
  // Sleep time does not guarantee that thread will be not finished or finished with edit of message
  // Solution? Threads need to join

}
