package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class FareMediaType {
  @SerialName("NONE")
  NONE,
  @SerialName("PAPER_TICKET")
  PAPER_TICKET,
  @SerialName("TRANSIT_CARD")
  TRANSIT_CARD,
  @SerialName("CONTACTLESS_EMV")
  CONTACTLESS_EMV,
  @SerialName("MOBILE_APP")
  MOBILE_APP,
  UNKNOWN_,
  ;

  public fun serialName(): String = FareMediaType.serializer().descriptor.getElementName(this.ordinal)
}
