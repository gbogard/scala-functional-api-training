package com.friends.domain

import java.time.Instant

trait Clock[F[_]] {

  def now(): F[Instant]

}
