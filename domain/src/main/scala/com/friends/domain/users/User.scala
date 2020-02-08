package com.friends.domain.users

import java.util.UUID

case class User(
  id: User.Id,
  userName: String,
  displayName: String,
  bio: Option[String]
)

object User {
  case class Id(value: UUID) extends AnyVal
}
