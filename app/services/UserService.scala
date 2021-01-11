package services

import javax.inject.{Inject, Singleton}
import models.User
import repositories.UserRepository

import scala.concurrent.Future

@Singleton
class UserService @Inject()(userRepository: UserRepository) {
  def get(id: Long): Future[Option[User]] = userRepository.get(id)
}
