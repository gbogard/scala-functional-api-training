package com.friends.application

import cats.effect.IO
import com.friends.application.commands._
import com.friends.domain.activities.ActivityWithContent
import com.friends.domain.posts.Post
import com.friends.domain.users.User
import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe._

object Serialization {

  implicit lazy val postEncoder: Encoder[Post] = deriveEncoder
  implicit lazy val createPostDecoder: Decoder[CreatePost] = deriveDecoder
  implicit lazy val userEncoder: Encoder[User] = deriveEncoder
  implicit lazy val signupDecoder: Decoder[Signup] = deriveDecoder
  implicit lazy val userIdCodec: Codec[User.Id] = deriveUnwrappedCodec
  implicit lazy val postIdCodec: Codec[Post.Id] = deriveUnwrappedCodec
  implicit lazy val activityWithContentEncoder: Encoder[ActivityWithContent] = deriveEncoder

  // Derive an EntityDecoder for all types which have a circe decoder
  implicit def http4sDecoder[T: Decoder]: EntityDecoder[IO, T] = jsonOf[IO, T]

  // Derive an EntityEncoder for all types which have a circe encoder
  implicit def http4sEncoder[T: Encoder]: EntityEncoder[IO, T] = jsonEncoderOf[IO, T]
}
