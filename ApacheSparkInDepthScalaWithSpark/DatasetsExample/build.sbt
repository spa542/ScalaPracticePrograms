lazy val root = (project in file("."))
  .settings(
    name := "DatasetsExample",
    scalaVersion := "2.12.0",
    version := "0.0.1"
  )

val sparkVersion = "3.2.3"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
// https://mvnrepository.com/artifact/org.apache.spark/spark-hive
// Provided means that the dependency will be excluded from the jar and is required at runtime on machine that runs the jar
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
  //"org.apache.spark" %% "spark-hive" % sparkVersion % "provided"
)
