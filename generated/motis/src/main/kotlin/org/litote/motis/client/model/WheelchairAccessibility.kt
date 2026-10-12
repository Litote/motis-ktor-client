package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class WheelchairAccessibility {
  @SerialName("ACCESSIBLE")
  ACCESSIBLE,
  @SerialName("NOT_ACCESSIBLE")
  NOT_ACCESSIBLE,
  UNKNOWN_,
  ;

  public fun serialName(): String = WheelchairAccessibility.serializer().descriptor.getElementName(this.ordinal)
}
