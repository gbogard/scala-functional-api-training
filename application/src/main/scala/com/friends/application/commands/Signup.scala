package com.friends.application.commands

case class Signup (
  userName: String,
  displayName: String,
  bio: Option[String],
  password: String
)
