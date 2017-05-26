name := "logs-lambda"

lazy val commonSettings = Seq(
  organization := "com.dyerusalimtsev",
  version := "1.0",
  scalaVersion := "2.12.2"
)

lazy val logProducer = (project in file("log-producer"))
  .settings(
    commonSettings,

    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1"
    )
  )