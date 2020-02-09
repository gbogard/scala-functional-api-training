package com.friends.domain

import scala.util.Try


trait Passwords {

  def hashPassword(input: String): Try[String]

  def isPasswordValid(input: String, hash: String): Try[Boolean]
}
