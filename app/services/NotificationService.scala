package services

import akka.Done
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future

@Singleton
class NotificationService @Inject()() {

  def send(message: String): Future[Done] = ???
}
