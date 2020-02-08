package com.friends.domain.posts

trait PostRepository[F[_]] {

  def storePost(post: Post): F[Post]

}
