package com.friends.application

import cats.effect.IO
import com.friends.application.Serialization._
import com.friends.application.commands.Signup
import com.friends.domain.users.SignupError.BelowMinimumAge
import com.friends.domain.users.{UserRepository, UserService}
import com.friends.domain.{Clock, IdGenerator, Passwords}
import org.http4s._
import org.http4s.dsl.io._

object UserController {

  def routes(implicit repository: UserRepository[IO],
             idGenerator: IdGenerator[IO],
             passwords: Passwords,
             clock: Clock[IO]): HttpRoutes[IO] = HttpRoutes.of {
    case req @ POST -> Root =>
      req
        .as[Signup]
        .flatMap(
          command =>
            UserService
              .signup[IO](
                command.userName,
                command.displayName,
                command.bio,
                command.password,
                command.birthDate
            ).value
        ).flatMap({
        case Right(user) => Ok(user)
        case Left(BelowMinimumAge) => BadRequest(s"You must be at least ${UserService.minimumAge} to sign up")
      })
  }
}
