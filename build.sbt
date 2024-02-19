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
      "dev.zio" %% "zio-logging" % "2.1.15",
      "io.getquill"          %% "quill-jdbc-zio" % "4.8.0",
      "org.postgresql"       %  "postgresql"     % "42.3.1",
      "dev.zio" %% "zio-json" % "0.6.2",
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion
    )
  )
