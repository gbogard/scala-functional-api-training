package com.friends.application

import cats.data.Kleisli
import cats.implicits._
import cats.effect._
import com.friends.domain.posts.PostRepository
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {

  implicit val postRepository: PostRepository[IO] = com.friends.infrastructure.PostRepositoryInterpreter

  val healthCheckRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok("Friends Server is running!")
  }

  val app: Kleisli[IO, Request[IO], Response[IO]] = Router(
    "/" -> healthCheckRoutes,
    "/posts" -> PostController.routes
  ).orNotFound

  val serverBuilder: BlazeServerBuilder[IO] = BlazeServerBuilder[IO].bindHttp(8080, "localhost")
    .withHttpApp(app)

  override def run(args: List[String]): IO[ExitCode] =
   serverBuilder.resource.use(_ => IO.never).as(ExitCode.Success)
}
