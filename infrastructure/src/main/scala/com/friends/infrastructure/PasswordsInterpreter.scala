package com.friends.infrastructure

import com.friends.domain.Passwords
import com.github.t3hnar.bcrypt._

import scala.util.Try

object PasswordsInterpreter extends Passwords {

  private val bcryptRounds = 10

  def hashPassword(input: String): Try[String] = input.bcryptSafe(bcryptRounds)

  def isPasswordValid(input: String, hash: String): Try[Boolean] = input.isBcryptedSafe(hash)
}
