package demo.part3Concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference
import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}
import scala.collection.parallel.immutable.ParVector

object ParallelUtils extends App {

  // 1 - Parallel Collections

  val parList = List(1,2,3).par // transforms this list into a parallel version of this list

  val parVector = ParVector[Int](1,2,3,4,5) // Can use Par"CollectionName"

  /*
    Can create parallel versions of...
    Seq
    Vector
    Array
    Map - Hash, Try, etc
    Set - Hash, Try, etc
   */

  // Using byName parameter here so the function is not evaluated until it is called between the two time functions
  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }

  val list = (1 to 10000).toList
  val serialTime = measure {
    list.map(_ + 1)
  }
  println("serial time: " + serialTime) // better when list is smaller because starting and stopping threads is costly
  val parallelTime = measure {
    list.par.map(_ + 1)
  }
  println("parallel time: " + parallelTime) // better by roughly a factor of 2 (when in millions)

  /*
    Map-reduce Model
    - split the elements into chunks (partitions) - Splitter (Map)
    - operation
    - recombine results - Combiner (Reduce)
   */

  // map, flatMap, filter, foreach, reduce, fold

  println(List(1,2,3).reduce(_ - _))
  println(List(1,2,3).par.reduce(_ - _)) // Not same result because we do not know the order in which the operands will be brought in to each thread
  // ****Be careful with fold and reduce (operations may not be associative!!!!)

  // Synchronization
  var sum = 0
  List(1,2,3).par.foreach(sum += _)
  println(sum) // 6 is not guaranteed (be very careful for race conditions)

  // Configuring
  parVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2)) // set the task support to 2 threads
  /*
    alternatives
      - ThreadPoolTaskSupport - deprecated
      - ExecutionContextTaskSupport(EC)
   */

  // Can create your own task support
//  parVector.tasksupport = new TaskSupport {
//    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???
//
//    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???
//
//    override def parallelismLevel: Int = ???
//
//    // Environment will manage your own thread
//    override val environment: AnyRef = _
//  }

  // 2 - Atomic Operations and References
  // Atomic operations cannot be intercepted by another thread or process (must go through and through or not at all)

  // Have atomic operations associated with class
  val atomic = new AtomicReference[Int](2)

  val currentValue = atomic.get() // thread-safe read
  atomic.set(4) // thread-safe write

  atomic.getAndSet(5) // will extract the current value and set it to a new value in a thread-safe way

  // if value is 38, then set the value to 56, otherwise, do nothing
  // this does shallow equality (reference equality)
  atomic.compareAndSet(38, 56)

  // Set a new value inside with a function and return the value (thread-safe)
  atomic.updateAndGet(_ + 1) // thread-safe function run
  atomic.getAndUpdate(_ + 1) // get the old value and then run a function (thread-safe)

  // Will add the first argument to atomic, then set, and then get it
  atomic.accumulateAndGet(12, _ + _) // a two parameter function and an argument (thread-safe)
  // Reverse
  atomic.getAndAccumulate(15, _ + _) // thread-safe

}
