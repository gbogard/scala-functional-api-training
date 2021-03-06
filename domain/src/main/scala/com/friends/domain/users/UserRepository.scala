package com.friends.domain.users

trait UserRepository[F[_]] {

  def storeUser(user: User, hashedPassword: String): F[User]

  def isUserNameAvailable(userName: String): F[Boolean]
}
