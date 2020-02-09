package com.friends.domain.posts

sealed trait PostAttachment

object PostAttachment {
  case class Images(images: List[String]) extends PostAttachment
  case class Link(title: String, description: String, image: String) extends PostAttachment
}

