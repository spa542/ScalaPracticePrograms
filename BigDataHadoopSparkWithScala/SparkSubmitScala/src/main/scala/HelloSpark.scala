import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

// Testing Hello Spark in Scala
object HelloSpark {

  def main(args: Array[String]): Unit = {
    // Create a SparkSession
    val ss: SparkSession = SparkSession.builder().master("yarn").appName("hello_spark_test").getOrCreate()
    // Create a dataframe
    val sample_df: RDD[Int] = ss.sparkContext.parallelize(List(1,2,3,4,5,6,7,8,9))
    val modified_df: RDD[Int] = sample_df.map(_ + 10)
    sample_df.saveAsTextFile("hdfs:///spark/sample_df.txt")
    modified_df.saveAsTextFile("hdfs:///spark/modified_df.txt")
  }

}
