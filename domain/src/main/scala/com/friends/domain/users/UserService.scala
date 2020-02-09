package com.friends.domain.users

import java.time.{LocalDate, ZoneId}

import cats.implicits._
import cats.data.EitherT
import cats.{Monad, MonadError}
import com.friends.domain.users.SignupError.{BelowMinimumAge, UserNameAlreadyExists}
import com.friends.domain.{Clock, IdGenerator, Passwords}

object UserService {

  val minimumAge = 13

  def signup[F[_]: Monad](
    userName: String,
    displayName: String,
    bio: Option[String],
    password: String,
    birthDate: LocalDate
  )(implicit idGenerator: IdGenerator[F],
    userRepository: UserRepository[F],
    passwords: Passwords,
    clock: Clock[F],
    ME: MonadError[F, Throwable]): EitherT[F, SignupError, User] =
    for {
      now <- EitherT.right(clock.now())
      isAboveMinAge = now.isAfter(
        birthDate.plusYears(minimumAge.toLong).atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant
      )
      _ <- {
        if (isAboveMinAge) EitherT.rightT[F, SignupError](())
        else EitherT.leftT[F, Unit](BelowMinimumAge)
      }
      _ <- EitherT(userRepository.isUserNameAvailable(userName) map {
        case true => Right(())
        case false => Left(UserNameAlreadyExists)
      })
      id <- EitherT.right(idGenerator.generateUserId())
      hashedPassword <- EitherT.right(
        ME.fromTry(passwords.hashPassword(password))
      )
      user = User(id, userName, displayName, bio)
      result <- EitherT.right(userRepository.storeUser(user, hashedPassword))
    } yield result

}
