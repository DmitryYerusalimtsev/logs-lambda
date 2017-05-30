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
