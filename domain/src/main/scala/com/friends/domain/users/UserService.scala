package com.friends.domain.users

import cats.implicits._
import cats.{Monad, MonadError}
import com.friends.domain.{IdGenerator, Passwords}

object UserService {

  def signup[F[_]: Monad](userName: String,
                          displayName: String,
                          bio: Option[String],
                          password: String,
  )(implicit idGenerator: IdGenerator[F],
    userRepository: UserRepository[F],
    passwords: Passwords,
    ME: MonadError[F, Throwable]): F[User] =
    for {
      id <- idGenerator.generateUserId()
      hashedPassword <- ME.fromTry(passwords.hashPassword(password))
      user = User(id, userName, displayName, bio)
      result <- userRepository.storeUser(user, hashedPassword)
    } yield result
}
