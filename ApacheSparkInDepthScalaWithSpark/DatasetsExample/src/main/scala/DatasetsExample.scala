import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._

// Create the dataset case class
case class Store(city: String, state: String, zip_code: Long)

case class State(state: String, stateName: String)

object DatasetsExample {

  def main(args: Array[String]): Unit = {
    // Create our session
    val ss = SparkSession.builder().master("yarn").appName("DatasetsExampleSparkApp").getOrCreate()

    // How to use toDF and toDS!!!!
    import ss.implicits._

    // Dataset examples
    // First get the dataframe
    val storeDF = ss.read.format("json").load("/dataframeExampleData/store_locations.json")
    storeDF.show(5)

    // Create the encoder for dataframe to dataset conversion
    val encoder = org.apache.spark.sql.Encoders.product[Store]

    // Convert the dataframe to a dataset using the set encoder
    val storeDS = storeDF.as(encoder)
    storeDS.show(5)

    // Another way to create a dataset using case class
    val ds1 = Seq(State("CA", "California")).toDS()
    ds1.show(5)
    ds1.printSchema()

    // Can get a dataframe back from a dataset operation
    val returnDF: DataFrame = storeDS.groupBy("city").count()
    returnDF.show(5)

  }

}
