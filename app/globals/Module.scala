package globals

import com.google.inject.AbstractModule
import play.api.{Configuration, Environment, Logging}

/**
  * Main Guice module
  * Add Configuration & Environment to Module Parameters when necessary
  */
class Module(environment: Environment, configuration: Configuration) extends AbstractModule with Logging {

  val _ = environment // Just to not break at compile time for not using the environment field.

}
