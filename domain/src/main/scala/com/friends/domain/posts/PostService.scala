package com.friends.domain.posts

import cats.Monad
import cats.implicits._
import com.friends.domain.{Clock, IdGenerator}
import com.friends.domain.users.User

object PostService {

  def createPost[F[_]: Monad](userId: User.Id, content: String)(
    implicit
    repository: PostRepository[F],
    idGenerator: IdGenerator[F],
    clock: Clock[F]
  ): F[Post] =
    for {
      id <- idGenerator.generatePostId()
      now <- clock.now()
      post = Post(
        id = id,
        createdAt = now,
        author = userId,
        content = content,
      )
      result <- repository.storePost(post)
    } yield result

  def getUserPosts[F[_]](
    userId: User.Id
  )(implicit repository: PostRepository[F]): F[List[Post]] =
    repository.getUserPosts(userId)
}
