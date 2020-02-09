package com.friends.application.commands

import java.time.LocalDate

case class Signup (
  userName: String,
  displayName: String,
  bio: Option[String],
  password: String,
  birthDate: LocalDate,
)
