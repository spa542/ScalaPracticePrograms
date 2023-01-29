import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._

object DatafameExample {

  def main(args: Array[String]): Unit = {
    val ss = SparkSession.builder().master("yarn").appName("DataframeExampleSparkApp").getOrCreate()

    // How to use toDF!!!!
    import ss.implicits._
    // How to use hive sql
    import ss.sql

    // TODO MAKE SURE TO MOVE DATA TO CORRECT LOCATION ON NODE
    val storeDF = ss.read.format("json").load("/dataframeExampleData/store_locations.json")
    println("Store Data")
    storeDF.show(5)

    println("Store Schema")
    storeDF.schema

    // Example manual schema (StructType ambiguity error atm)
    //val sampleManualSchema = StructType(StructField("city", StringType, true), StructField("state", StringType, true),
      //StructField("zip_code", LongType, true))

    // Columns
    storeDF.columns
    storeDF.col("city")
    storeDF.select(storeDF.col("city")).show(5)
    storeDF.select(expr("city")).show(5)
    storeDF.select(col("city")).show(5)
    storeDF.select(column("city")).show(5)
    storeDF.select(col("city").alias("city_name")).show(5)

    // Rows
    storeDF.collect()
    storeDF.show(10)
    storeDF.first()

    // Count
    println(storeDF.count())

    // If you know SQL, you can work on dataframe seamlessly
    // Create a view
    storeDF.createOrReplaceTempView("storeDFView")
    // Query the data
    ss.sql("select * from storeDFView")
    storeDF.select("city").show()
    //storeDF.sqlContext.sql("")

    // Create a dataframe directly from data
    val sampleData: RDD[Row] = ss.sparkContext.parallelize(Array(Row("Ryan", 23), Row("Joe", 25), Row("Mary", 62)))
    val myDF = ss.createDataFrame(sampleData, schema = new StructType().add(StructField("Name", StringType)).add(StructField("Age", IntegerType)))
    myDF.show()
    myDF.schema
    myDF.columns

    // Print Schema
    myDF.printSchema()

    // Giving a fixed value
    storeDF.select(expr("*"), lit("United States").as("country")).show(5)
    // Using withColumn
    storeDF.withColumn("country", lit("United States")).show(5)
    // withColumnRenamed (change the name of the column)
    storeDF.withColumnRenamed("city", "city_name_test").show(5)

    // one hot encoding
    storeDF.withColumn("is_Antioch", expr("city == 'Antioch'")).show(10)

    // Can change configurations to make spark sql case sensitive*

    // Dropping columns
    storeDF.drop("city", "state").show(5)

    // Change data type (type casting)
    storeDF.withColumn("zip_code", col("zip_code").cast("int")).show(5)

    // where clause (can also use filter)
    storeDF.select("city").where("city == 'Woodland'").show(5)
    storeDF.select("city").filter("city == 'Woodland'").show(5)

    // and - &&, or - ||, not - ! (in expressions)

    // Distinct values and count
    println(storeDF.select("state").distinct().count())

    // Sampling
    // sample(withReplacement, fraction/sampleSize, randSeed)
    storeDF.sample(withReplacement = true, .2, 30).show(5)
    // split and randomSplit (for ML training and testing)

    // Append data to a dataframe
    // union, intersection, difference (all set operations)

    // orderBy
    storeDF.orderBy(expr("city desc")).show(5)

    // repartition and coalesce (same as RDD) (.rdd converts to an rdd)
    println(storeDF.rdd.getNumPartitions)


  }

}
