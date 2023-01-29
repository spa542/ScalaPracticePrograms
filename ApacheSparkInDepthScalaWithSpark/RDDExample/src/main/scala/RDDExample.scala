import org.apache.spark.sql.SparkSession

object RDDExample {

  def main(args: Array[String]): Unit = {
    // SparkSession
    val ss = SparkSession.builder().master("yarn").appName("RDDExampleSpark").getOrCreate()

    // Create an RDD
    val sampleRDD = ss.sparkContext.parallelize(Array("BMW", "Bentley", "Mercedes", "Honda", "Jaguar", "BMW", "Mercedes", "Suzuki"))

    // Print the rdd
    sampleRDD.collect()

    // Distinct - give all of the distinct values in
    val distinctCarsRDD = sampleRDD.distinct()
    distinctCarsRDD.collect()

    // Filter - filter the cars in the RDD
    val filterCarsRDD = distinctCarsRDD.filter(carName => carName.startsWith("B"))
    // Other way
    val filterCarsRDDOther = distinctCarsRDD.filter(_.startsWith("B"))
    filterCarsRDD.collect()
    filterCarsRDDOther.collect()
    // Filter even numbers
    val numbersRDD = ss.sparkContext.parallelize(1 to 100)
    val evenNums = numbersRDD.filter(_ % 2 == 0)
    evenNums.collect()
    // Further filtering
    val carsWithBStart = filterCarsRDD.map(car => (car, car.startsWith("B")))
    carsWithBStart.collect()
    // Multiple arg filter
    val carsWithBStartFilter = carsWithBStart.filter(_._2 == true)
    carsWithBStartFilter.collect()

    // NOTE: Map will do a basic map transformation, flatMap will consolidate transformation into a single array

    // Random Split (For ML Training)
    val sampleNumRDD = ss.sparkContext.parallelize(1 to 100)
    // 70 - 30 Split
    val randSplit = sampleNumRDD.randomSplit(Array[Double](0.7, 0.3))
    randSplit(0).collect()
    randSplit(1).collect()

    // reduce - takes next element and adds/subtracts with the existing result
    // NOTE: Can add your own function to the reduce
    def reduceFunction(num1: Int, num2: Int): Int = {
      num1 * num2
    }
    val reduceSplitNum = randSplit(0).reduce(_+_)
    val reduceSplitCustomNum = randSplit(0).reduce(reduceFunction)
    println(reduceSplitNum)
    println(reduceSplitCustomNum)

    // Count
    println(sampleNumRDD.count())

    // countApprox
    // Count returned in 25 millisecond timeout (default confidence interval is 95%)
    println(sampleNumRDD.countApprox(23, .1)) // 23 second timeout, 90% CI

    // countByValue
    println(sampleNumRDD.countByValue())
    // countByValueApprox

    // Can explore myself
    // Other Actions (first, max, min, take, takeOrdered, top, takeSample)

    // saveAsTextFile (will be in HDFS) (unless specified otherwise)
    //sampleNumRDD.saveAsTextFile("hdfs://......")

    // cache and persist
    // Can use the cache for repeated transformations
    sampleNumRDD.cache() // cache the rdd (can be seen in the spark ui)
    // Caution! => Must watch out for caching because memory is limited
    // Instead, use persist
    // NOTE: Uses disk to cache (there are many different options)
    sampleNumRDD.persist()
    sampleNumRDD.unpersist()

    // Transformation (not executed) and action
    // Checkpoints - great if you have a lot of data
    ss.sparkContext.setCheckpointDir("/checkpointtest012923")
    sampleNumRDD.checkpoint() // Set the checkpoint here (will show up in HDFS)
    // Can then get the checkpoint to go from where you left off (with RDD)
    //ss.sparkContext.getCheckpointDir("/checkpointtest012923")

    // pipe
    val sampleBookRDD = ss.sparkContext.parallelize(Array("I love this book!"))
    // Can pipe your rdd into any of the linux commands and then get the output back
    sampleBookRDD.collect()
    sampleBookRDD.pipe("wc -l").collect()

    // mapPartitions (can use mapPartitions to do a map function "by partition" rather than for the whole RDD 1 by 1)
    // mapPartitionsWIthIndex (good for debug purpose) (can index to find what element is in each partition)
    // example
    def indexFunc(i: Int, wordInIterator: Iterator[String]) = {
      wordInIterator.toList.map(word => s"Partition:$i => $word").iterator
    }
    println("MapPartitions with Index")
    sampleBookRDD.mapPartitionsWithIndex(indexFunc).collect()

    // foreachPartition (operates foreach on each partition (works the same as mapPartitions)
    // foreachPartitionWithIndex (works same as mapPartitionsWithIndex)

    // key - value RDD
    // can create something like a dictionary in python (json)
    // Use map
    // Use keyBy (end value is the key)
    sampleBookRDD.keyBy(word => word.length).collect()
    val saveSampleKeyRDD = sampleBookRDD.keyBy(word => word.length)
    // Just like in Python
    saveSampleKeyRDD.keys.collect()
    saveSampleKeyRDD.values.collect()
    // can also use functions like mapValues and flatMapValues

    // lookup (by key)
    println(saveSampleKeyRDD.lookup(1))

    // Other helpful functions
    // countByKey and countByKeyApprox
    // groupByKey / groupBy (version 1) (does in driver program) (risk out of memory error!)
    saveSampleKeyRDD.groupByKey().map(z => (z._1, z._2.reduce(_ + _))).collect()
    // or use reduceByKey! same as ex above (1st do in executor and then do in driver) (much faster)
    saveSampleKeyRDD.reduceByKey(_ + _).collect()

    // aggregate
    val sampleNumbers = ss.sparkContext.parallelize(1 to 20, 4)
    sampleNumbers.collect()
    println("Printing the aggregate")
    // zero value, partitionWise, resultsOfPartitions
    println(sampleNumbers.aggregate(0)(_ + _, _ + _))
    // treeAggregate (same as aggregate but can specify the depth) (does more at the executor level)
    println(sampleNumbers.treeAggregate(0)(_ + _, _ + _, 3))
    // can also use aggregateByKey

    // cogroup (joining two RDD on basis of key and then value would be the flatMap of their values in each individual RDD)

    // joins (SQL joins)

    // Advanced RDD operations
    println("Starting advanced RDD operations...")

    // zips (need to have the same number of elements and same number of partitions)
    // using sampleRDD (cars)
    sampleRDD.collect()
    val sampleNumRDD2 = ss.sparkContext.parallelize(Array(10000, 100000, 5234, 23479823, 588888, 298323, 2398423, 6765773))
    sampleNumRDD2.collect()
    val zippedRDD = sampleRDD.zip(sampleNumRDD2)
    zippedRDD.collect()

    // coalesce (should use for all reduce acts!!!) (avoids reshuffling of data!)
    // only reduces number of partitions
    // repartition
    // reduce or increase the number of partitions

    // When to reduce?
    // you do a transformation and there is no need for multiple partitions (too many partitions)
    // saving a specific type of file
    // When to increase?
    // you want to increase the level of parallelism (sensing slowness or have a lack of memory)

    // customPartition (you can control what partition gets what data)
    // can also extend customPartition
    // Other partitioners (hash partitioner, range partitioner)
    // partitionBy
  }

}
