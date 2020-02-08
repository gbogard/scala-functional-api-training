package com.friends.infrastructure

import cats.effect.IO
import com.friends.domain.posts.{Post, PostRepository}

object PostRepositoryInterpreter extends PostRepository[IO] {

  def storePost(post: Post): IO[Post] = IO.pure(post)

}
