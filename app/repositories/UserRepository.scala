package repositories

import globals.ApplicationResult
import javax.inject.{Inject, Singleton}
import models.User
import repositories.tables.UserTable.userTable
import slick.jdbc.PostgresProfile.api._

@Singleton
class UserRepository @Inject()(repository: PostgresRepository) {

  def get(id: Long): ApplicationResult[Option[User]] =
    repository.run(userTable.filter(_.id === id).result.headOption)
}
