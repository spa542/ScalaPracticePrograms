import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructType}
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row


// Can define a schema from case class
case class TestSchema(date: String, name: String, age: Int)

// Second definition
case class TestHive(key: Int, value: String)


object SparkSQLMain {

  def main(args: Array[String]): Unit = {
    // Get the spark session
    // Will need to use .config to start up hive support
    // When using hive support, need to provide jar /usr/local/hive/hive-2.3.6/lib/mysql-connector-java.jar in command
    val ss = SparkSession.builder().master("yarn").config("spark.sql.warehouse.dir", "/user/hive/warehouse").enableHiveSupport().appName("spark_sql_hive").getOrCreate()

    // How to use toDF!!!!
    import ss.implicits._
    // How to use hive sql
    import ss.sql

    // Test rdd
    val a = ss.sparkContext.parallelize(1 to 10)

    //val res1: Array[Int] = Array((1 to 10))

    println("Using RDD")
    // Test res1
    //println(res1)
    // Test a
    a.collect()

    // Test b
    val b = a.map(x => (x, x+1))
    b.collect()

    println("Using Dataframe")
    // Test dataframe
    val df = ss.createDataFrame(b)
    df.show()

    // Dont need to registerTempTables anymore (old spark)

    // DF From list
    val ab = List(("Tom", 5), ("Jerry", 2), ("Donald", 7))
    val df2 = ss.createDataFrame(ab)
    df2.show()
    // DF from Seq with default schema
    val testSeq = Seq(("yo", 1), ("hello", 2), ("woo", 5), ("hi", 10))
    val anotherDf = ss.createDataFrame(testSeq)
    println("The whole df")
    anotherDf.show()
    println("Selecting the first default column")
    anotherDf.select("_1").show()
    //testSeq.toDF (can use implicits to get this here)
    // Converting to named dataframe (work off of the testSeq (first create as RDD))
    val rowRDD: RDD[Row] = ss.sparkContext.parallelize(testSeq).map(currItem => Row(currItem._1, currItem._2))
    val newSchema = new StructType()
      .add(StructField("Word", StringType))
      .add(StructField("Age", IntegerType))
    // For some reason isnt working
    val schemaDF = ss.createDataFrame(rowRDD, newSchema)
    println("Schema DF selecting first column")
    schemaDF.show()
    schemaDF.select("Age").show()

    // Can define a schema from a case class
    // See case class up above
    val testDataRDD = ss.sparkContext.parallelize(List(
      Row("1/2/20", "Ryan", 25),
      Row("10/24/22", "Donald", 60),
      Row("5/6/07", "ReRe", 52)
    ))
    // Not a practical example, but can be used to turn data into respective data types and make into case class
    // Normally would start with a read in file or something
    val newTestDataRDD = testDataRDD.map(row => TestSchema(row(0).toString, row(1).toString, row(2).asInstanceOf[Int]))
    // From case class
    println("From Case Class")
    // **You could add a schema that is also created like above
    ss.createDataFrame(newTestDataRDD).show()

    // **can load data from various file formats (most are built in, some may need a jar provided)

    // More examples are found in cloudera vm

    // Hive support examples out of date and need to be reworked
    // dummy data
    val hive_df = ss.createDataFrame((1 to 50).map(i => TestHive(i, s"val_$i")))
    hive_df.write.mode("overwrite").saveAsTable("default.test_hive")
    // See the data (gets a dataframe back
    val returnHiveDF = sql("SELECT * FROM default.test_hive")
    returnHiveDF.printSchema()
    returnHiveDF.show()
  }

}
