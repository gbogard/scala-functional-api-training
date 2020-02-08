import sbt._

object Dependencies {
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
  val cats = "org.typelevel" %% "cats-core" % "2.0.0"
  val catsEffect = "org.typelevel" %% "cats-effect" % "2.1.1"

  object Http4s {
    private val version = "0.21.0-RC4"

    val dsl = "org.http4s" %% "http4s-dsl" % version
    val blazeServer = "org.http4s" %% "http4s-blaze-server" % version
    val blazeClient = "org.http4s" %% "http4s-blaze-client" % version
    val circeIntegration = "org.http4s" %% "http4s-circe" % version

    val all: Seq[ModuleID] =
      Seq(dsl, blazeServer, blazeClient, circeIntegration)
  }

  object Circe {
    private val version = "0.12.3"

    val core = "io.circe" %% "circe-core" % version
    val generic = "io.circe" %% "circe-generic" % version
    val genericExtras = "io.circe" %% "circe-generic-extras" % "0.12.2"
    val parser = "io.circe" %% "circe-parser" % version

    val all: Seq[ModuleID] = Seq(core, generic, parser, genericExtras)
  }

  object Database {
    private val doobieVersion = "0.8.8"

    val doobieCore = "org.tpolecat" %% "doobie-core" % doobieVersion
    val doobieHikari = "org.tpolecat" %% "doobie-hikari" % doobieVersion
    val doobiePg = "org.tpolecat" %% "doobie-postgres" % doobieVersion
    val driver = "org.postgresql" % "postgresql" % "42.2.10"

    val all = Seq(doobieCore, doobieHikari, doobiePg, driver)
  }

  object Log {
    val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

    val all = Seq(logback, scalaLogging)
  }

}
