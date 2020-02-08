package com.friends.infrastructure

import cats.implicits._
import cats.effect.IO
import com.friends.domain.posts.{Post, PostRepository}
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.implicits.legacy.instant._

class PostRepositoryInterpreter(implicit xa: Transactor[IO]) extends PostRepository[IO] {

  def storePost(post: Post): IO[Post] =
    sql"""
      insert into posts (id, created_at, author_id, content)
      values (${post.id.value}, ${post.createdAt}, ${post.author.value}, ${post.content})
    """.update.run.transact(xa).as(post)

}
