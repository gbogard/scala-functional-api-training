package com.friends.application

import cats.effect.IO
import com.friends.application.Serialization._
import com.friends.application.commands.Signup
import com.friends.domain.users.{UserRepository, UserService}
import com.friends.domain.{IdGenerator, Passwords}
import org.http4s._
import org.http4s.dsl.io._

object UserController {

  def routes(implicit repository: UserRepository[IO], idGenerator: IdGenerator[IO], passwords: Passwords): HttpRoutes[IO] = HttpRoutes.of {
    case req @ POST -> Root =>
      req
        .as[Signup]
        .flatMap(
          command => UserService.signup[IO](
            command.userName,
            command.displayName,
            command.bio,
            command.password
          )
        )
        .flatMap(Ok(_))
  }
}
