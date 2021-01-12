package services

import akka.Done
import akka.Done.done
import javax.inject.{Inject, Singleton}
import play.api.Logging

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NotificationService @Inject()()(implicit ec: ExecutionContext) extends Logging {

  // TODO implement using kafka
  def send(message: String): Future[Done] = Future {
    logger.info(s"Sending notification with message: $message")
    done()
  }
}
