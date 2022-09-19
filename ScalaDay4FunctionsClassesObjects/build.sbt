ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaDay4FunctionsClassesObjects"
  )

// https://mvnrepository.com/artifact/com.vertica/vertica-jdbc
libraryDependencies += "com.vertica" % "vertica-jdbc" % "9.1.1"