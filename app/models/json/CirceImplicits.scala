package models.json

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder}
import io.circe.{Decoder, Encoder, Printer}
import models.Transaction
import models.response.CreateTransactionResponse

trait CirceImplicits {
  implicit val customPrinter: Printer      = Printer.noSpaces.copy(dropNullValues = true)
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit lazy val transactionEncoder: Encoder[Transaction] = deriveConfiguredEncoder
  implicit lazy val transactionDecoder: Decoder[Transaction] = deriveConfiguredDecoder

  implicit lazy val createTransactionResponseDecoder: Decoder[CreateTransactionResponse] = deriveConfiguredDecoder
}
