package com.friends.infrastructure

import cats.implicits._
import cats.effect.IO
import cats.Show
import com.friends.domain.activities.{Activity, ActivityData, ActivityRepository}
import com.friends.domain.users.User
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.implicits.legacy.instant._
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import io.circe.parser._
import ActivityRepositoryInterpreter._
import cats.data.NonEmptyList
import doobie.util.{Get, Put}
import org.postgresql.util.PGobject

class ActivityRepositoryInterpreter(implicit xa: Transactor[IO]) extends ActivityRepository[IO] {
  def storeActivity(activity: Activity): IO[Activity] =
    sql"""
      insert into activities (id, created_at, user_id, content)
      values(${activity.id}, ${activity.createdAt}, ${activity.userId}, ${activity.activityData})
    """.update.run.transact(xa).as(activity)

  def getUserActivities(userId: User.Id): IO[List[Activity]] =
    sql"""
      select id, created_at, user_id, content from activities
      where user_id = $userId
      order by created_at desc
    """.query[Activity].to[List].transact(xa)
}

object ActivityRepositoryInterpreter {
  implicit val activityDataCodec: Codec[ActivityData] = deriveCodec[ActivityData]

  implicit val showPGobject: Show[PGobject] = Show.show(_.getValue.take(250))

  implicit val jsonGet: Get[Json] =
    Get.Advanced.other[PGobject](NonEmptyList.of("json")).temap[Json] { o =>
      parse(o.getValue).leftMap(_.show)
    }

  implicit val jsonPut: Put[Json] =
    Put.Advanced.other[PGobject](NonEmptyList.of("json")).tcontramap[Json] { j =>
      val o = new PGobject
      o.setType("json")
      o.setValue(j.noSpaces)
      o
    }

  implicit val activityDataGet: Get[ActivityData] = Get[Json].temap(_.as[ActivityData].leftMap(_.getMessage))
  implicit val activityDataPut: Put[ActivityData] = Put[Json].contramap(_.asJson)
}
