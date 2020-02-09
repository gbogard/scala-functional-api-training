package com.friends.domain.posts

import java.time.Instant
import java.util.UUID

import com.friends.domain.users.User

object PostService {

  def createPost[F[_]](userId: User.Id, content: String)(implicit repository: PostRepository[F]): F[Post] = {
    val post = Post(
      id = Post.Id(UUID.randomUUID()),
      createdAt = Instant.now(),
      author = userId,
      content = content,
    )
    repository.storePost(post)
  }

  def getUserPosts[F[_]](userId: User.Id)(implicit repository: PostRepository[F]): F[List[Post]] =
    repository.getUserPosts(userId)
}
