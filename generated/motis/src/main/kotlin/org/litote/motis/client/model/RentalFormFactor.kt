package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class RentalFormFactor {
  @SerialName("BICYCLE")
  BICYCLE,
  @SerialName("CARGO_BICYCLE")
  CARGO_BICYCLE,
  @SerialName("CAR")
  CAR,
  @SerialName("MOPED")
  MOPED,
  @SerialName("SCOOTER_STANDING")
  SCOOTER_STANDING,
  @SerialName("SCOOTER_SEATED")
  SCOOTER_SEATED,
  @SerialName("OTHER")
  OTHER,
  UNKNOWN_,
  ;

  public fun serialName(): String = RentalFormFactor.serializer().descriptor.getElementName(this.ordinal)
}
