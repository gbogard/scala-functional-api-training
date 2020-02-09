package com.friends.infrastructure

import cats.effect.IO
import cats.implicits._
import com.friends.domain.users.{User, UserRepository}
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.postgres.implicits._

class UserRepositoryInterpreter(implicit xa: Transactor[IO]) extends UserRepository[IO] {

  def storeUser(user: User, hashedPassword: String): IO[User] =
    sql"""
       insert into users (id, user_name, display_name, bio, password)
       values (${user.id}, ${user.userName}, ${user.displayName}, ${user.bio}, $hashedPassword)
    """.update.run.transact(xa).as(user)

}
