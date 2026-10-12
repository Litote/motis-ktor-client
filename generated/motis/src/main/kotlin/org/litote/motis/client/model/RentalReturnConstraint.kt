package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class RentalReturnConstraint {
  @SerialName("NONE")
  NONE,
  @SerialName("ANY_STATION")
  ANY_STATION,
  @SerialName("ROUNDTRIP_STATION")
  ROUNDTRIP_STATION,
  UNKNOWN_,
  ;

  public fun serialName(): String = RentalReturnConstraint.serializer().descriptor.getElementName(this.ordinal)
}
