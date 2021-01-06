package controllers

import controllers.circe.Decodable
import javax.inject.{Inject, Singleton}
import io.circe.syntax._
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
    with CirceImplicits {

  def getAll = Action.async { _ =>
    transactionService
      .getAll()
      .map { transactions =>
        Ok(transactions.asJson)
      }
  }
}
