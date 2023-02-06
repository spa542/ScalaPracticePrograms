import org.apache.spark.sql.SparkSession

// Second definition
case class StudRow(key: Int, value: String)

object SparkSQLExample {

  def main(args: Array[String]): Unit = {
    // Need to specify a couple extra commands to incorporate hive with Spark SQL
    val ss = SparkSession.builder().master("yarn").config("spark.sql.warehouse.dir", "/user/hive/warehouse").enableHiveSupport().appName("spark_sql_example").getOrCreate()

    // How to use toDF!!!!
    import ss.implicits._
    // How to use hive sql (shorthand)
    import ss.sql

    // Can select and and show databases / tables just as you would in mysql console
    sql("USE default") // select the correct database

    // Create a sample DB
    val hive_df = ss.createDataFrame((1 to 50).map(i => StudRow(i, s"val_$i")))
    hive_df.write.mode("overwrite").saveAsTable("test_hive_stud")
    // Read the sample DB
    val returnHiveDF = sql("SELECT * FROM test_hive_stud")

    returnHiveDF.show(5)

    // Can play around using any sql commands through the sql interface

    // Using the store_locations.json data from HDFS
    // Use """ if you are going to use "" in the SQL command
    // TODO make sure store_locations.json is in correct location
    sql("""CREATE TABLE IF NOT EXISTS store_locations (city STRING, state STRING, zip_code STRING) USING JSON OPTIONS (path "/dataframeExampleData/store_locations.json")""")
    sql("""SHOW TABLES""").show()

    // Select and show
    sql("select * from store_locations").show(5)

    // Create a managed table
    sql("create table if not exists store_locations_m using parquet as select * from store_locations")
    sql("select * from store_locations_m").show(5)

    // Can partition by specific columns

    // Can use the temp view to create a temporary table in order to do queries
    // These examples can be used in other example files in this project

    // Can use other things like sqoop and other apache products (see differences in tutorials)

    // Can cache and uncache tables
  }

}
