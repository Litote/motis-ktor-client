package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class LocationType {
  @SerialName("ADDRESS")
  ADDRESS,
  @SerialName("PLACE")
  PLACE,
  @SerialName("STOP")
  STOP,
  UNKNOWN_,
  ;

  public fun serialName(): String = LocationType.serializer().descriptor.getElementName(this.ordinal)
}
