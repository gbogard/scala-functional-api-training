package com.friends.application

import java.util.UUID

import cats.effect.IO
import com.friends.application.Serialization._
import com.friends.application.commands.Signup
import com.friends.domain.activities.{ActivityRepository, ActivityService}
import com.friends.domain.posts.PostRepository
import com.friends.domain.users.SignupError.{BelowMinimumAge, UserNameAlreadyExists}
import com.friends.domain.users.{User, UserRepository, UserService}
import com.friends.domain.{Clock, IdGenerator, Passwords}
import org.http4s._
import org.http4s.dsl.io._

import scala.util.Try

object UserController {

  def routes(implicit userRepository: UserRepository[IO],
             activityRepository: ActivityRepository[IO],
             postRepository: PostRepository[IO],
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
        case Left(UserNameAlreadyExists) => Conflict("Username already exists")
      })
    case GET -> Root / userId / "feed" =>
      Try(UUID.fromString(userId)).fold(
        _ => BadRequest(s"$userId is not a valid activity id"),
        uuid => ActivityService.getActivityFeed[IO](User.Id(uuid)).flatMap(Ok(_))
      )
  }
}
