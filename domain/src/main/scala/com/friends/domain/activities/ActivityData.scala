package com.friends.domain.activities

import com.friends.domain.posts.Post
import com.friends.domain.users.User

sealed trait ActivityData

object ActivityData {
  case class Posted(authorId: User.Id, postId: Post.Id) extends ActivityData
  case object SignedUp extends ActivityData
}
