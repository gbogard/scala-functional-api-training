package com.friends.domain

import com.friends.domain.activities.Activity
import com.friends.domain.posts.Post
import com.friends.domain.users.User

trait IdGenerator[F[_]] {

  def generateUserId(): F[User.Id]

  def generatePostId(): F[Post.Id]

  def generateActivityId(): F[Activity.Id]

}
