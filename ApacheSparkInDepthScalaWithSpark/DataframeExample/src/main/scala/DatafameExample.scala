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

    // TODO MAKE SURE TO DOWNLOAD AND MOVE DATA TO CORRECT LOCATION ON NODE
    val storeDF = ss.read.format("json").load("/dataframeExampleData/store_locations.json")
    // TODO MAKE SURE TO DOWNLOAD AND MOVE DATA TO CORRECT LOCATION ON NODE
    val carDF = ss.read.format("csv").option("header", "true").option("inferSchema", "true")
      .load("/dataframeExampleData/CarPrice_Assignment.csv")

    // Starting with storeDF from here on out
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

    // Starting with carDF from here on out
    carDF.createOrReplaceTempView("carDFView")
    ss.sql("""select * from carDFView""").show(1)

    carDF.select("*").show(1)

    // List the car names where price > 30000 (can do either way)
    carDF.where("price > 30000").select("CarName", "price").show(5)
    ss.sql("""select CarName, price form carDFView where price > 30000""").show(5)

    // Find the combined average of highway mpg and city mpg for all cars
    val avgMPG = (col("citympg") + col("highwaympg")) / 2
    // One Way (if using col, must use col for EVERY expression)
    carDF.select(col("CarName"), col("highwaympg"), col("citympg"), avgMPG.alias("avgmpg")).show(5)
    // Another way (can trea each string as its own expression)
    carDF.selectExpr("CarName", "highwaympg", "citympg", "((citympg + highwaympg) / 2) AS avgmpg").show(5)

    // Find average price
    carDF.select(avg("price")).show(5)
    // Find average price statistics
    carDF.select(avg("price")).describe().show(5)

    // Spark SQL Functions
    // Can use upper(), lower(), and camel() as a wrap for each column
    // Can use trim(), ltrim(), and rtrim()
    // Can use regex
    carDF.select(col("CarName")).show(1)
    // Create your regex
    val extractStr = "(alfa|bmw|audi)"
    // Use your regex (example)
    // regexp_extract, regexp_replace
    carDF.select(regexp_extract(col("CarName"), extractStr, 1).alias("extracted_data"), col("CarName")).show(10)
    // contains(), replace()

    // If the schema column does not match the schema, the column will be dropped****
    // Ex. If any column in a given row is a null, entire row will be dropped here
    println(carDF.na.drop("all").count())

    // Replace with column name
    println(carDF.na.fill("DUMMY VALUE", Seq("CarName", "carbody")).count())

    // Can use more structured data by using StructType, ArrayType, and others

    // explode
    // exploding involves decomposing an array / struct to create a row for each element in the array / struct with the
    // attributes of the original row for each new sub row
    // Very bad for performance but can be a helpful tool

    // Can read a value of a json as a txt file and then query the "key" as column name
    // json_tuple will give you the value for the given key
    // *There are also other json functions that you can use (explore self)
    val countryDF = ss.range(1).selectExpr(""" '{"Singapore": {"Tier1": ["Yishun", "Bishan", "Woodlands"]}}' as jsonColumn""")
    countryDF.printSchema()
    countryDF.collect()
    countryDF.select(json_tuple(col("jsonColumn"), "Singapore") as "extracted_data").show()

    // Timestamps
    // Look into date time function in spark functions

    // UDF (User Defined Function
    val tempCarDF = carDF.select(col("compressionratio") as "premium")
    tempCarDF.show(5)
    // Define a UDF that calculates the return on investment
    // Sample UDF
    def roi(x: Double): Double = x * 5 / 3
    println(roi(10))
    // Create the UDF
    val roiUDF = udf(roi(_: Double): Double)
    // User the UDF
    tempCarDF.select(col("premium"), roiUDF(col("premium")).alias("roi")).show(5)

    // Joins
    // Inner Join - rows that include the common inner joined column
    // Left outer Join - rows that include teh common inner joined column and left hand side columns
    // Right outer Join - rows that include the common inner joined column and right hand side columns
    // Full Outer Join (Outer Join) - left, right, and inner
    // Left Semi Join - Only include left inner joins
    // Right Semi Join - Only include right inner joins
    // Natural Join - it will automatically join on all the fields having the same attributes (no specification of fields to join on)
    // **Can do examples elsewhere

    // More transformation actions

    // Can use spark in-built functions or use external libraries in order to read in files

    // To convert file formats, need to read in current format and then do an overwrite in a new format

  }

}
