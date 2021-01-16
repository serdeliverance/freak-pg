package repositories

import globals.ApplicationResult
import javax.inject.{Inject, Singleton}
import models.errors.DataBaseError
import play.api.Logging
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.dbio.{DBIOAction, NoStream}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PostgresRepository @Inject()(val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile]
    with Logging {

  def run[R](action: DBIOAction[R, NoStream, Nothing]): ApplicationResult[R] =
    db.run(action)
      .map(Right(_))
      .recover({
        case e =>
          logger.error("Error to run query", e)
          Left(DataBaseError("Error to run query"))
      })
}
