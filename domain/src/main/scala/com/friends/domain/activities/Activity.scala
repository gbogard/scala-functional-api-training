package com.friends.domain.activities

import java.time.Instant
import java.util.UUID

import com.friends.domain.users.User

case class Activity(
  id: Activity.Id,
  createdAt: Instant,
  userId: User.Id,
  activityData: ActivityData,
)

object Activity {
  case class Id(value: UUID)
}
