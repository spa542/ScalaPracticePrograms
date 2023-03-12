lazy val root = (project in file("."))
  .settings(
    name := "AdvancedScalaAndFunctionalProgramming",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.12.0"
  )

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)
