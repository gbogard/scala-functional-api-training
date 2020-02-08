import Dependencies._

ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.friends"
ThisBuild / organizationName := "Friends"

lazy val domain = project
  .settings(
    libraryDependencies ++= Seq(
      cats,
      scalaTest
    )
  )

lazy val infrastructure = project
  .settings(
    libraryDependencies ++= Seq(
      scalaTest,
      cats,
      catsEffect
    ) ++ Circe.all ++ Database.all
  ).settings({
    val databaseUser = sys.env.getOrElse("FRIENDS_DB_USER", "friends")
    val databaseName = sys.env.getOrElse("FRIENDS_DB_NAME", "friends")
    val databaseHost = sys.env.getOrElse("FRIENDS_DB_HOST", "localhost")
    val databasePort = sys.env.getOrElse("FRIENDS_DB_PORT", "5445")
    val databasePassword = sys.env.getOrElse("FRIENDS_DB_PASSWORD", "secret")

    Seq(
      flywayUrl := s"jdbc:postgresql://$databaseHost:$databasePort/$databaseName",
      flywayUser := databaseUser,
      flywayPassword := databasePassword,
    )
  })
  .enablePlugins(FlywayPlugin)
  .dependsOn(domain)

lazy val application = project
  .settings(
    libraryDependencies ++= Seq(
      scalaTest,
      cats,
      catsEffect
    ) ++ Circe.all ++ Http4s.all
  ).dependsOn(domain, infrastructure)

lazy val root = (project in file(".")).aggregate(domain, infrastructure, application)