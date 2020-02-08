package com.friends.infrastructure

import cats.effect._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object Transactor {

  def apply()(implicit cs: ContextShift[IO]): Resource[IO, HikariTransactor[IO]] = {
    val databaseUser = sys.env.getOrElse("FRIENDS_DB_USER", "friends")
    val databaseName = sys.env.getOrElse("FRIENDS_DB_NAME", "friends")
    val databaseHost = sys.env.getOrElse("FRIENDS_DB_HOST", "localhost")
    val databasePort = sys.env.getOrElse("FRIENDS_DB_PORT", "5445")
    val databasePassword = sys.env.getOrElse("FRIENDS_DB_PASSWORD", "secret")

    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](32)
      be <- Blocker[IO]
      xa <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = "org.postgresql.Driver",
        url = s"jdbc:postgresql://$databaseHost:$databasePort/$databaseName",
        user = databaseUser,
        pass = databasePassword,
        connectEC = ce,
        blocker = be,
      )
    } yield xa
  }

}
