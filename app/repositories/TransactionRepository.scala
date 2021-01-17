package repositories

import globals.ApplicationResult
import javax.inject.{Inject, Singleton}
import models.Transaction
import play.api.Logging
import repositories.tables.TransactionTable.transactionTable
import slick.jdbc.PostgresProfile.api._

@Singleton
class TransactionRepository @Inject()(repository: PostgresRepository) extends Logging {

  def getAll(): ApplicationResult[Seq[Transaction]] =
    repository.run(transactionTable.result)

  def getByUser(userId: Long): ApplicationResult[Seq[Transaction]] = {
    val query = transactionTable.filter(_.userId === userId)
    repository.run(query.result)
  }

  def save(transaction: Transaction): ApplicationResult[Int] = {
    logger.info("Saving transaction")
    repository.run(transactionTable += transaction)
  }
}
