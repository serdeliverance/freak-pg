package models

import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder}
import io.circe.{Decoder, Encoder}
import models.json.CirceImplicits

case class Transaction(
    id: Option[Long],
    amount: BigDecimal,
    cardLast4Digits: String,
    dateTime: String,
    installments: Int,
    cardType: String,
    userId: Long,
    status: Option[String])
