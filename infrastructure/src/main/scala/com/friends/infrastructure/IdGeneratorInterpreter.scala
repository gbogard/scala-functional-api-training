package com.friends.infrastructure

import java.util.UUID

import cats.effect.IO
import com.friends.domain.IdGenerator
import com.friends.domain.posts.Post
import com.friends.domain.users.User

object IdGeneratorInterpreter extends IdGenerator[IO] {
  def generateUserId(): IO[User.Id] = IO(UUID.randomUUID()).map(User.Id)

  def generatePostId(): IO[Post.Id] = IO(UUID.randomUUID).map(Post.Id)
}
