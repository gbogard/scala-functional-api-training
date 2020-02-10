package com.friends.domain.posts

import com.friends.domain.users.User

trait PostRepository[F[_]] {

  def storePost(post: Post): F[Post]

  def getUserPosts(userId: User.Id): F[List[Post]]

  def getPostById(id: Post.Id): F[Option[Post]]

}
