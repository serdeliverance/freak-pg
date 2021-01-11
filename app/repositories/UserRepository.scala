package repositories

import javax.inject.{Inject, Singleton}
import models.User
import repositories.tables.UserTable.userTable
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

@Singleton
class UserRepository @Inject()(repository: PostgresRepository) {

  def get(id: Long): Future[Option[User]] = repository.run(userTable.filter(_.id === id).result.headOption)
}
