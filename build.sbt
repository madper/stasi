val scala3Version = "3.3.1"
val zioVersion = "2.1-RC1"
val circeVersion = "0.14.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "stasi",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-streams" % zioVersion,
      "io.nats" % "jnats" % "2.17.0",
      "dev.zio" %% "zio-logging" % "2.2.1",
      "io.getquill"          %% "quill-jdbc-zio" % "4.8.0",
      "org.postgresql"       %  "postgresql"     % "42.3.1",
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "ch.qos.logback" % "logback-classic" % "1.5.0",
      // SLF4j v1 integration
      "dev.zio" %% "zio-logging-slf4j" % "2.2.1"
    )
  )
