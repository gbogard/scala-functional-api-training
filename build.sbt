import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.friends"
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
    ) ++ Circe.all
  ).dependsOn(domain)

lazy val application = project
  .settings(
    libraryDependencies ++= Seq(
      scalaTest,
      cats,
      catsEffect
    ) ++ Circe.all ++ Http4s.all
  ).dependsOn(domain, infrastructure)

lazy val root = (project in file(".")).aggregate(domain, infrastructure, application)