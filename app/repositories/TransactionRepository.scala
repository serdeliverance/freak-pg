package repositories

import javax.inject.{Inject, Singleton}
import models.Transaction
import play.api.Logging
import repositories.tables.TransactionTable.transactionTable
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

@Singleton
class TransactionRepository @Inject()(repository: PostgresRepository) extends Logging {

  def getAll(): Future[Seq[Transaction]] =
    repository.run(transactionTable.result)

  def getByUser(userId: Long): Future[Seq[Transaction]] = {
    val query = transactionTable.filter(_.userId === userId)
    repository.run(query.result)
  }

  def save(transaction: Transaction): Future[Int] = {
    logger.info("Saving transaction")
    repository.run(transactionTable += transaction)
  }
}
