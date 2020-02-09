package com.friends.domain.users

import java.time.{Instant, LocalDate}

import cats.{Id, MonadError}
import com.friends.domain._
import com.friends.domain.users.SignupError.{BelowMinimumAge, UserNameAlreadyExists}
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class UserServiceSpec extends FunSpec with MockFactory with Matchers {

  describe("The Signup method") {
    it("Should fail when the user is under 13") {

      (clock.now _).when().returns(Instant.now())

      val result = UserService.signup[Id](
        userName = "john",
        displayName = "John",
        bio = Some("i love dogs"),
        password = "password",
        birthDate = LocalDate.parse("2013-10-10")
      )

      result.value shouldBe Left(BelowMinimumAge)
    }

    it("Should fail when the username is already taken") {

      (clock.now _).when().returns(Instant.now())
      (userRepository.isUserNameAvailable _).when("john").returns(false)

      val result = UserService.signup[Id](
        userName = "john",
        displayName = "John",
        bio = Some("i love dogs"),
        password = "password",
        birthDate = LocalDate.parse("1990-10-10")
      )

      result.value shouldBe Left(UserNameAlreadyExists)
    }
  }

  implicit lazy val clock: Clock[Id] = stub[Clock[Id]]
  implicit lazy val idGenerator: IdGenerator[Id] = stub[IdGenerator[Id]]
  implicit lazy val passwords: Passwords = stub[Passwords]
  implicit lazy val userRepository: UserRepository[Id] = stub[UserRepository[Id]]
  implicit lazy val monadError: MonadError[Id, Throwable] = new MonadError[Id, Throwable] {
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
    def tailRecM[A, B](a: A)(f: A => Id[Either[A, B]]): Id[B] = ???
    def raiseError[A](e: Throwable): Id[A] = ???
    def handleErrorWith[A](fa: Id[A])(f: Throwable => Id[A]): Id[A] = ???
    def pure[A](x: A): Id[A] = x
  }
}
