package com.friends.domain.activities

import com.friends.domain.users.User

trait ActivityRepository[F[_]] {

  def storeActivity(activity: Activity): F[Activity]

  def getUserActivities(userId: User.Id): F[Seq[Activity]]
}
