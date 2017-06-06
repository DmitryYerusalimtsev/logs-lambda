name := "logs-lambda"

lazy val commonSettings = Seq(
  organization := "com.dyerusalimtsev",
  version := "1.0",
  scalaVersion := "2.11.8"
)

val sparkVersion = "1.6.0"

val sparkDependencies = Seq(
  "org.apache.spark" % "spark-core_2.11" % sparkVersion % "provided",
  "org.apache.spark" % "spark-sql_2.11" % sparkVersion % "provided",
  "org.apache.spark" % "spark-streaming_2.11" % sparkVersion,
  "org.apache.spark" % "spark-streaming-kafka_2.11" % sparkVersion
)

val kafkaDependencies = Seq(
  "org.apache.kafka" % "kafka_2.11" % "0.8.2.1",
  "org.apache.kafka" % "kafka-clients" % "0.8.2.1"
)

val cassandraDependencies = Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.0.1",
  "com.datastax.spark" % "spark-cassandra-connector_2.11" % "1.6.1"
)

val algebird = "com.twitter" % "algebird-core_2.11" % "0.11.0"
val tsConfig = "com.typesafe" % "config" % "1.3.1"

lazy val core = (project in file("core"))
  .settings(
    commonSettings,

    libraryDependencies ++= sparkDependencies ++ Seq(algebird)
  )

lazy val logProducer = (project in file("log-producer"))
  .settings(
    commonSettings,

    libraryDependencies ++= kafkaDependencies ++ Seq(
      tsConfig,
      "commons-io" % "commons-io" % "2.4"
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

    libraryDependencies ++= sparkDependencies
      ++ cassandraDependencies
      ++ Seq(
      tsConfig
    )
  ).dependsOn(core, utils)

lazy val speed = (project in file("speed"))
  .settings(
    commonSettings,

    libraryDependencies ++= sparkDependencies
      ++ cassandraDependencies
      ++ Seq(tsConfig, algebird)
  ).dependsOn(core, utils)


assemblyMergeStrategy in assembly := {
  case x if x.contains(".class") => MergeStrategy.last
  case x if x.endsWith(".properties") => MergeStrategy.last
  case x if x.contains("/resources/") => MergeStrategy.last
  case x if x.startsWith("META-INF/mailcap") => MergeStrategy.last
  case x if x.startsWith("META-INF/mimetypes.default") => MergeStrategy.first
  case x if x.startsWith("META-INF/maven/org.slf4j/slf4j-api/pom.") => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    if (oldStrategy == MergeStrategy.deduplicate)
      MergeStrategy.first
    else
      oldStrategy(x)
}
