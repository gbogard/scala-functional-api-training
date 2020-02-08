package com.friends.application

import cats.effect.IO
import com.friends.application.commands.CreatePost
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import com.friends.domain.posts.{PostRepository, PostService}
import io.circe.syntax._
import io.circe.generic.auto._

object PostController {

  implicit val createPostCommandDecoder = jsonOf[IO, CreatePost]

  def routes(implicit repository: PostRepository[IO]): HttpRoutes[IO] = HttpRoutes.of {
    case req @ POST -> Root =>
      req.as[CreatePost]
        .flatMap(command => PostService.createPost(command.userId, command.content))
        .map(_.asJson)
        .flatMap(Ok(_))
  }
}
