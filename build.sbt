name := "logs-lambda"

lazy val commonSettings = Seq(
  organization := "com.dyerusalimtsev",
  version := "1.0",
  scalaVersion := "2.11.8"
)

val sparkVersion = "1.6.0"

val sparkDependencies = Seq(
  "org.apache.spark" % "spark-core_2.11" % sparkVersion,
  "org.apache.spark" % "spark-sql_2.11" % sparkVersion
)

lazy val logProducer = (project in file("log-producer"))
  .settings(
    commonSettings,

    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1"
    )
  )

lazy val utils = (project in file("utils"))
  .settings(
    commonSettings,

    libraryDependencies ++= sparkDependencies
  )


lazy val batch = (project in file("batch"))
  .settings(
    commonSettings,

    libraryDependencies ++= sparkDependencies ++ Seq(
      "com.typesafe" % "config" % "1.3.1"
    )
  ).dependsOn(utils)
