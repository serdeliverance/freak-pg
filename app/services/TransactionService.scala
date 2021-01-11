package services

import javax.inject.{Inject, Singleton}
import models.Transaction
import repositories.TransactionRepository

import scala.concurrent.Future

@Singleton
class TransactionService @Inject()(transactionRepository: TransactionRepository) {

  def getAll(): Future[Seq[Transaction]] = transactionRepository.getAll()

  def getByUser(userId: Long): Future[Seq[Transaction]] = transactionRepository.getByUser(userId)
}
