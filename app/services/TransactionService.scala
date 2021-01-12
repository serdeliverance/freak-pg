package services

import akka.Done
import akka.Done.done
import configuration.CREDIT_CARD_API_URL
import io.circe.parser.decode
import io.circe.syntax._
import javax.inject.{Inject, Singleton}
import models.Transaction
import models.json.CirceImplicits
import models.response.CreateTransactionResponse
import play.api.{Configuration, Logging}
import play.api.libs.ws.WSClient
import repositories.TransactionRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TransactionService @Inject()(
    configuration: Configuration,
    transactionRepository: TransactionRepository,
    userService: UserService,
    notificationService: NotificationService,
    wsClient: WSClient
  )(implicit ec: ExecutionContext)
    extends Logging
    with CirceImplicits {

  private val creditCardApiUrl = configuration.get[String](CREDIT_CARD_API_URL)

  def getAll(): Future[Seq[Transaction]] = transactionRepository.getAll()

  def getByUser(userId: Long): Future[Seq[Transaction]] = transactionRepository.getByUser(userId)

  def create(transaction: Transaction): Future[Done] =
    validateUser(transaction.userId)
      .flatMap { _ =>
        sendToCreditCard(transaction)
          .flatMap { creditCardResponse =>
            transactionRepository
              .save(transaction.copy(status = Some(creditCardResponse.status)))
              .flatMap { _ =>
                notificationService.send("transaction created")
              }
          }
      }

  private def validateUser(userId: Long): Future[Done] =
    userService
      .get(userId)
      .map {
        case Some(_) =>
          logger.info("User validation success")
          done()
        case _ =>
          logger.info("User does not exists")
          throw new IllegalArgumentException("User does not exists")
      }

  private def sendToCreditCard(transaction: Transaction): Future[CreateTransactionResponse] =
    wsClient
      .url(creditCardApiUrl)
      .post(transaction.asJson.toString)
      .flatMap { response =>
        decode[CreateTransactionResponse](response.body)
          .fold(
            _ => {
              logger.info("Failed to communicate with API or decoding response")
              Future.failed(new IllegalArgumentException("failed to decoding response"))
            },
            resp => {
              logger.info(s"Credit card api has ${resp.status} the transaction")
              Future.successful(resp)
            }
          )
      }
}
