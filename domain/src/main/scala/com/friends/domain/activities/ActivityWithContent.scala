package com.friends.domain.activities

import com.friends.domain.posts.Post

case class ActivityWithContent (
  activity: Activity,
  post: Option[Post]
)
