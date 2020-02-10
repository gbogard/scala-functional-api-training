package com.friends.domain.posts

import cats.Monad
import cats.implicits._
import com.friends.domain.activities.{Activity, ActivityData, ActivityRepository}
import com.friends.domain.{Clock, IdGenerator}
import com.friends.domain.users.User

object PostService {

  def createPost[F[_]: Monad](userId: User.Id, content: String)(
    implicit
    postRepository: PostRepository[F],
    activityRepository: ActivityRepository[F],
    idGenerator: IdGenerator[F],
    clock: Clock[F]
  ): F[Post] =
    for {
      postId <- idGenerator.generatePostId()
      activityId <- idGenerator.generateActivityId()
      now <- clock.now()
      post = Post(
        id = postId,
        createdAt = now,
        author = userId,
        content = content,
      )
      result <- postRepository.storePost(post)
      activity = Activity(
        id = activityId,
        createdAt = now,
        userId = userId,
        activityData = ActivityData.Posted(userId, postId)
      )
      _ <- activityRepository.storeActivity(activity)
    } yield result

  def getUserPosts[F[_]](
    userId: User.Id
  )(implicit repository: PostRepository[F]): F[List[Post]] =
    repository.getUserPosts(userId)
}
