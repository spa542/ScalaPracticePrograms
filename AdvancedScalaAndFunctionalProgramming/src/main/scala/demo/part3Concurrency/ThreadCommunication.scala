package demo.part3Concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
  The Producer Consumer Problem Review

  producer -> [ x ] -> consumer
   */
  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def set(newValue: Int) = value = newValue
    def get = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while(container.isEmpty) {
        println("[consumer] actively waiting...")
      }

      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produced, after long work, the value " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  //naiveProdCons()

  // Wait and Notify
  // Entering a synchronized expression on an object locks the object (mutex)
  // **only AnyRefs can have synchronized blocks
  // **no assumptions about who gets the lock first
  // **keep locking to a minimum
  // **maintain thread safety at all times in parallel programming
  // wait() ing on an objects monitor suspends the thread indefinitely
  // notify() will call a sleeping thread that is wait() ing on an objects monitor
  // notifyAll() to awaken all threads
  // Waiting and notifying ONLY WORKS in SYNCHRONIZED expressions or will crash code
  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")

      container.synchronized {
        container.wait()
      }

      // container must have some value at this point (been waiting and will be woken up by producer)
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] Hard at work...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println("[producer] I'm producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

  //smartProdCons()

  /*
  producer -> [ ? ? ? ] -> consumer

  Both producer and consumer may block each other
   */
  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while(true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE empty space in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  //prodConsLargeBuffer()

  /*
  producer1 -> [ ? ? ? ] -> consumer1
  producer2 -----^    ^----- consumer2
  ...
  ...
   */
  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          // Could run into issue where one producer and one consumer call wake each other up back and forth
          // Therefore, change if to while; make sure the consumer is awake and the buffer is empty
          // Multiple producers could wake up multiple consumers but only 1 is available to consume
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer $id] consumed " + x)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE empty space in the buffer
          println(s"[producer $id] producing " + i)
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int, capacity: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]()

    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
  }

  //multiProdCons(3, 3, 20)

  /*
  Exercises
  1) Think of an example where notifyAll acts in a different way than notify
  2) Create a deadlock
    - threads block each other and the program cannot continue
  3) Create a livelock
    - threads are active and have yielded execution to each other yet none of them can continue
   */

  // notifyAll
  def testNofifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting...")
        bell.wait()
        println(s"[thread $i] hooray!")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] rock and roll!")
      bell.synchronized {
        bell.notifyAll()
        //bell.notify() // Not everyone will wake up
      }
    }).start()
  }

  //testNofifyAll()

  // deadlock
  case class Friend(name: String) {
    def bow(other: Friend) = {
      this.synchronized {
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }

    def rise(other: Friend) = {
      this.synchronized {
        println(s"$this: I am rising to my friend $other")
      }
    }

    var side = "right"
    def switchSide(): Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {
      while(this.side == other.side) {
        println(s"$this: Oh, but please $other, feel free to pass")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val sam = Friend("Sam")
  val pierre = Friend("Pierre")

//  new Thread(() => sam.bow(pierre)).start() // sam's lock, then pierre's lock
//  new Thread(() => pierre.bow(sam)).start() // pierre's lock, then sam's lock

  // livelock
//  new Thread(() => sam.pass(pierre)).start()
//  new Thread(() => pierre.pass(sam)).start()


}
