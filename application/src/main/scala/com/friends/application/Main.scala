package com.friends.application

import cats.implicits._
import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello $name!")
  }

  val serverBuilder: BlazeServerBuilder[IO] = BlazeServerBuilder[IO].bindHttp(8080, "localhost")
    .withHttpApp(routes.orNotFound)

  override def run(args: List[String]): IO[ExitCode] =
   serverBuilder.resource.use(_ => IO.never).as(ExitCode.Success)
}
