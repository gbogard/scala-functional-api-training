package com.friends.application.commands

import com.friends.domain.users.User

case class CreatePost(
  userId: User.Id,
  content: String
)
