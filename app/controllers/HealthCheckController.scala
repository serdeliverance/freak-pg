package controllers

import javax.inject.Singleton
import play.api.mvc.{Action, AnyContent, InjectedController}

@Singleton
class HealthCheckController extends InjectedController {

  def check: Action[AnyContent] = Action(Ok("running"))
}
