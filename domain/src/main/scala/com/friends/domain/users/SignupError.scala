package com.friends.domain.users

sealed trait SignupError

object SignupError {
  case object BelowMinimumAge extends SignupError
}
