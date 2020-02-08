package com.friends.domain.posts

import java.time.Instant
import java.util.UUID

import com.friends.domain.users.User

case class Post(
  id: Post.Id,
  createdAt: Instant,
  author: User.Id,
  content: String,
)

object Post {
  case class Id(value: UUID) extends AnyVal
}