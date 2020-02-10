package com.friends.infrastructure

import java.util.UUID

import cats.effect.IO
import com.friends.domain.IdGenerator
import com.friends.domain.activities.Activity
import com.friends.domain.posts.Post
import com.friends.domain.users.User

object IdGeneratorInterpreter extends IdGenerator[IO] {
  private val randomUUID = IO(UUID.randomUUID())

  def generateUserId(): IO[User.Id] = randomUUID map User.Id

  def generatePostId(): IO[Post.Id] = randomUUID map Post.Id

  def generateActivityId(): IO[Activity.Id] = randomUUID map Activity.Id
}
