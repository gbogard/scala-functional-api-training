package com.friends.domain.activities

import cats.implicits._
import cats.Monad
import com.friends.domain.posts.PostRepository
import com.friends.domain.users.User

object ActivityService {

  def getActivityFeed[F[_]: Monad](userId: User.Id)(
    implicit postRepository: PostRepository[F],
    activityRepository: ActivityRepository[F]
  ): F[List[ActivityWithContent]] = for {
    activities <- activityRepository.getUserActivities(userId)
    posts <- activities.map(_.activityData).collect({
      case ActivityData.Posted(_, postId) => postId
    }).traverseFilter(postRepository.getPostById)
    activitiesWithContent = activities.map(activity => {
      val post = activity.activityData match {
        case ActivityData.Posted(_, postId) => posts.find(_.id == postId)
        case _ => None
      }

      ActivityWithContent(activity, post)
    })
  } yield activitiesWithContent
}
