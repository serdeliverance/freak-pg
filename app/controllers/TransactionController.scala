package controllers

import akka.Done
import controllers.circe.Decodable
import controllers.converters.ErrorToResultConverter
import globals.{ApplicationResult, EitherResult}
import io.circe.syntax._
import javax.inject.{Inject, Singleton}
import models.Transaction
import models.errors.ApplicationError
import models.json.CirceImplicits
import play.api.Logger
import play.api.mvc.{BaseController, ControllerComponents, Result}
import services.TransactionService

import scala.concurrent.{ExecutionContext, Future}

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
      .flatMap { result =>
        mapEitherResult[Done](result)(_ => Created, handleError)("Transaction created successfully",
                                                                 "error creating transaction")(logger)
      }
  }

  private def mapEitherResult[T](
      appResult: EitherResult[T]
    )(handleSuccess: T => Result,
      handleError: ApplicationError => Result
    )(successMsg: String = "Operation success",
      errorMsg: String = "Operation failed"
    )(implicit logger: Logger
    ): Future[Result] =
    appResult.map {
      case Right(result) =>
        logger.info(successMsg)
        handleSuccess(result)
      case Left(error) =>
        logger.info(errorMsg)
        handleError(error)
    }
}
