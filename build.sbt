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
  "org.apache.spark" % "spark-streaming_2.11" % sparkVersion
)

val tsConfig = "com.typesafe" % "config" % "1.3.1"

lazy val logProducer = (project in file("log-producer"))
  .settings(
    commonSettings,

    libraryDependencies ++= Seq(
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

    libraryDependencies ++= sparkDependencies ++ Seq(
      tsConfig
    )
  ).dependsOn(utils)

lazy val speed = (project in file("speed"))
  .settings(
    commonSettings,

    libraryDependencies ++= sparkDependencies ++ Seq(
      tsConfig
    )
  ).dependsOn(utils)


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
