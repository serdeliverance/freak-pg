package services

import akka.Done
import akka.Done.done
import configuration.CREDIT_CARD_API_URL
import globals.ApplicationResult._
import globals.{ApplicationResult, ApplicationResultExtended}
import io.circe.parser.decode
import io.circe.syntax._
import javax.inject.{Inject, Singleton}
import models.Transaction
import models.errors.{ClientError, ExternalServiceError, GenericError}
import models.json.CirceImplicits
import models.response.CreateTransactionResponse
import play.api.libs.ws.WSClient
import play.api.{Configuration, Logging}
import repositories.TransactionRepository
import cats.implicits._

import scala.concurrent.ExecutionContext

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

  def getAll(): ApplicationResult[Seq[Transaction]] = transactionRepository.getAll()

  def getByUser(userId: Long): ApplicationResult[Seq[Transaction]] = transactionRepository.getByUser(userId)

  def create(transaction: Transaction): ApplicationResult[Done] = {
    for {
      _    <- validateUser(transaction.userId).toEitherT()
      resp <- sendToCreditCard(transaction).toEitherT()
      _    <- transactionRepository.save(transaction.copy(status = Some(resp.status))).toEitherT()
      _    <- notificationService.send("transaction created").toEitherT()
    } yield Done
  }.value

  private def validateUser(userId: Long): ApplicationResult[Done] =
    userService
      .get(userId)
      .map {
        case Some(_) =>
          logger.info("User validation success")
          Right(done())
        case _ =>
          logger.info("User does not exists")
          Left(GenericError("User does not exits"))
      }

  private def sendToCreditCard(transaction: Transaction): ApplicationResult[CreateTransactionResponse] =
    wsClient
      .url(creditCardApiUrl)
      .post(transaction.asJson.toString)
      .flatMap { response =>
        response.status match {
          case 200 =>
            decode[CreateTransactionResponse](response.body)
              .fold(
                _ => {
                  logger.info("Failed to communicate with external service")
                  error(ExternalServiceError)
                },
                resp => {
                  logger.info(s"Credit card api has ${resp.status} the transaction")
                  ApplicationResult(resp)
                }
              )
          case 400 =>
            logger.info("Missing argument or payload")
            error(ClientError("Malformed data or payload"))
        }
      }

}
