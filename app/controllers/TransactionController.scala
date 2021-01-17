package controllers

import controllers.circe.Decodable
import controllers.converters.ErrorToResultConverter
import globals._
import io.circe.syntax._
import javax.inject.{Inject, Singleton}
import models.Transaction
import models.errors.ApplicationError
import models.json.CirceImplicits
import play.api.mvc.{BaseController, ControllerComponents, Result}
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
      .mapToResult(
        transactions => Ok(transactions.asJson),
        defaultErrorHandlerWithMessage("Error getting all transactions")
      )
  }

  def getByUser(userId: Long) = Action.async { _ =>
    transactionService
      .getByUser(userId)
      .mapToResult(
        transactions => {
          Ok(transactions.asJson)
        },
        defaultErrorHandlerWithMessage(s"Error getting transactions for userId: $userId")
      )
  }

  def create() = Action.async(decode[Transaction]) { request =>
    transactionService
      .create(request.body)
      .mapToResult(_ => {
        logger.info("Transaction created")
        Created
      }, defaultErrorHandlerWithMessage("Error creating transaction"))
  }

  private def defaultErrorHandlerWithMessage(msg: String): ApplicationError => Result =
    error => {
      logger.info(msg)
      handleError(error)
    }
}
