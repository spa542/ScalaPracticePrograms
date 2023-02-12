import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.functions._

object SparkStreamingExample {

  def main(args: Array[String]): Unit = {
    // Need to specify a couple extra commands to incorporate hive with Spark SQL
    val ss = SparkSession.builder().master("yarn").config("spark.sql.warehouse.dir", "/user/hive/warehouse").enableHiveSupport().appName("spark_streaming_example").getOrCreate()

    // For encoders
    import ss.implicits._

    // This example (to work) will need to be connected to some sort of IoT so that data can be transferred.
    // This example is purely for show until some IoT is connected
    // Can change these options as needed
    // TODO: Can use "nc -lk 9999" to write words to the port for stream example
    val lines = ss.readStream.format("socket").option("host", "localhost").option("port", "9999").load()

    // Split strings by spaces
    val words = lines.as[String].flatMap(_.split(" "))

    // Count the # of words
    val wordCounts = words.groupBy("value").count()

    println(wordCounts)

    // Write streamed output to the console
    val query = wordCounts.writeStream.outputMode("complete").format("console").start()
    // Write streamed output to the console based on trigger
    val query2 = wordCounts.writeStream.outputMode("complete").format("console").trigger(Trigger.ProcessingTime("60 seconds")).start()

    // Other Example (Reading from storage file that is save in HDFS)
    // TODO Need to make sure data is actually being written to that folder in HDFS that can be used
    // TODO Use given activity data that is provided by course and place in the correct folder in HDFS
    val activityData = ss.read.json("/dataframeExampleData/activity_data/")
    // Get the schema
    val activitySchema = activityData.schema
    // Create the stream with the given schema
    val activityDataStream = ss.readStream.schema(activitySchema).option("maxFilesPerTrigger", 1).json("/dataframeExampleData/activity_data/")
    // Start the query
    val activityQuery = activityDataStream.writeStream.queryName("activity_count_stream_query").format("console").outputMode("complete").start()
    val prevAvg = activityData.groupBy("gt").avg()
    // Compare the average of the previous data to what the current streaming data is
    val activityQuery2 = activityDataStream.join(prevAvg, "gt").writeStream.queryName("activity_count_stream_query").format("console").outputMode("complete").start()
    // Query with checkpoints
    val activityQuery3 = activityDataStream.writeStream.queryName("activity_count_stream_query").format("console").outputMode("complete").option("checkpointLocation", "/spark/checkpoint").option("truncate", false).start()
    // Query with Stateful Processing and Watermarks
    // Window size is 10 minutes because we are analyzing every 10 minutes
    val activityCountStream = activityDataStream.selectExpr("*", "cast(cast(Creation_Time as double)/1000000000 as timestamp) as event_time").withWatermark("event_time", "30 minutes").groupBy(window(col("event_time"), "10 minutes", "5 minutes")).count()
    val activityCountQuery = activityCountStream.writeStream.queryName("activity_count_Stream").format("console").outputMode("complete").option("truncate", false).start()
    // Query with removing duplicates
    val activityCountStream2 = activityDataStream.selectExpr("*", "cast(cast(Creation_Time as double)/1000000000 as timestamp) as event_time").dropDuplicates("User", "event_time").withWatermark("event_time", "30 minutes").groupBy(window(col("event_time"), "10 minutes", "5 minutes")).count()
    val activityCountQuery2 = activityCountStream2.writeStream.queryName("activity_count_stream_drop_duplicates").format("console").outputMode("complete").option("truncate", false).start()
  }

}
