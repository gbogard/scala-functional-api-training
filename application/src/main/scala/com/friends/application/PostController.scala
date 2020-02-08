package com.friends.application

import cats.effect.IO
import com.friends.application.commands.CreatePost
import org.http4s._
import org.http4s.dsl.io._
import com.friends.domain.posts.{PostRepository, PostService}
import com.friends.application.Serialization._

object PostController {

  def routes(implicit repository: PostRepository[IO]): HttpRoutes[IO] = HttpRoutes.of {
    case req @ POST -> Root =>
      req.as[CreatePost]
        .flatMap(command => PostService.createPost(command.userId, command.content))
        .flatMap(Ok(_))
  }
}
