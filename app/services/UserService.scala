package services

import globals.ApplicationResult
import javax.inject.{Inject, Singleton}
import models.User
import repositories.UserRepository

@Singleton
class UserService @Inject()(userRepository: UserRepository) {
  def get(id: Long): ApplicationResult[Option[User]] = userRepository.get(id)
}
