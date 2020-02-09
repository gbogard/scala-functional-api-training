package com.friends.infrastructure

import java.time.Instant

import cats.effect.IO
import com.friends.domain.Clock

object ClockInterpreter extends Clock[IO] {
  def now(): IO[Instant] = IO(Instant.now())
}
