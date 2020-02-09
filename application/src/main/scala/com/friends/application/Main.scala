package com.friends.application

import cats.data.Kleisli
import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp}
import com.friends.domain.{Clock, IdGenerator, Passwords}
import com.friends.domain.posts.PostRepository
import com.friends.domain.users.UserRepository
import com.friends.infrastructure._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = Transactor() use { implicit xa =>

    implicit val postRepository: PostRepository[IO] = new PostRepositoryInterpreter()
    implicit val userRepository: UserRepository[IO] = new UserRepositoryInterpreter()
    implicit val passwords: Passwords = PasswordsInterpreter
    implicit val clock: Clock[IO] = ClockInterpreter
    implicit val idGenerator: IdGenerator[IO] = IdGeneratorInterpreter

    val healthCheckRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
      case GET -> Root => Ok("Friends Server is running!")
    }

    val app: Kleisli[IO, Request[IO], Response[IO]] = Router(
      "/" -> healthCheckRoutes,
      "/posts" -> PostController.routes,
      "/users" -> UserController.routes,
    ).orNotFound

    val serverBuilder: BlazeServerBuilder[IO] = BlazeServerBuilder[IO].bindHttp(8080, "localhost")
      .withHttpApp(app)

    serverBuilder.resource.use(_ => IO.never).as(ExitCode.Success)
  }
}
