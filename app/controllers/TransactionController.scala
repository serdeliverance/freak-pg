package controllers

import controllers.circe.Decodable
import controllers.converters.ErrorToResultConverter
import io.circe.syntax._
import javax.inject.{Inject, Singleton}
import models.Transaction
import models.json.CirceImplicits
import play.api.mvc.{BaseController, ControllerComponents}
import services.TransactionService

import scala.concurrent.ExecutionContext

@Singleton
class TransactionController @Inject()(
    val controllerComponents: ControllerComponents,
    transactionService: TransactionService
  )(implicit ec: ExecutionContext)
    extends BaseController
    with Decodable
    with CirceImplicits
    with ErrorToResultConverter {

  def getAll = Action.async { _ =>
    transactionService
      .getAll()
      .map {
        case Right(transactions) =>
          Ok(transactions.asJson)
        case Left(error) =>
          logger.info("Error retrieving transactions")
          handleError(error)
      }
  }

  def getByUser(userId: Long) = Action.async { _ =>
    transactionService
      .getByUser(userId)
      .map {
        case Right(transactions) =>
          Ok(transactions.asJson)
        case Left(error) =>
          logger.info(s"Error retrieving transactions for user :$userId")
          handleError(error)
      }
  }

  def create() = Action.async(decode[Transaction]) { request =>
    transactionService
      .create(request.body)
      .map { _ =>
        Created
      }
  }
}
