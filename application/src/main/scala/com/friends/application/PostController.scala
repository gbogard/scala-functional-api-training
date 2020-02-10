package com.friends.application

import java.util.UUID

import cats.effect.IO
import com.friends.application.commands.CreatePost
import org.http4s._
import org.http4s.dsl.io._
import com.friends.domain.posts.{PostRepository, PostService}
import com.friends.application.Serialization._
import com.friends.domain.activities.ActivityRepository
import com.friends.domain.{Clock, IdGenerator}
import com.friends.domain.users.User

import scala.util.Try

object PostController {

  def routes(implicit postRepository: PostRepository[IO],
             activityRepository: ActivityRepository[IO],
             clock: Clock[IO],
             idGenerator: IdGenerator[IO]): HttpRoutes[IO] = HttpRoutes.of {
    case req @ POST -> Root =>
      req
        .as[CreatePost]
        .flatMap(
          command => PostService.createPost[IO](command.userId, command.content)
        )
        .flatMap(Ok(_))
    case GET -> Root / "user" / userId =>
      Try(UUID.fromString(userId)).fold(
        _ => BadRequest(s"$userId is not a valid user id"),
        uuid => PostService.getUserPosts(User.Id(uuid)).flatMap(Ok(_))
      )
  }
}
