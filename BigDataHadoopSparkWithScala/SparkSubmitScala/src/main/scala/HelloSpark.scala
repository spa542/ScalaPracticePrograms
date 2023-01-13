import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

// Testing Hello Spark in Scala
object HelloSpark {

  def main(args: Array[String]): Unit = {
    // Create a SparkSession
    val ss: SparkSession = SparkSession.builder().master("yarn").appName("hello_spark_test").getOrCreate()
    // Create a dataframe
    val sample_df: RDD[Int] = ss.sparkContext.parallelize(List(1,2,3,4,5,6,7,8,9))
    println(sample_df.collect().mkString)
    val modified_df: RDD[Int] = sample_df.map(_ + 10)
    println(modified_df.collect().mkString)
    sample_df.saveAsTextFile("./sample_df.txt")
    modified_df.saveAsTextFile("./modified_df.txt")
  }

}