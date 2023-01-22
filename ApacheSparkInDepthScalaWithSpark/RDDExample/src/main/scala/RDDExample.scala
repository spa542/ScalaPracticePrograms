import org.apache.spark.rdd.RDD
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






  }

}
