package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class ElevationCosts {
  @SerialName("NONE")
  NONE,
  @SerialName("LOW")
  LOW,
  @SerialName("HIGH")
  HIGH,
  UNKNOWN_,
  ;

  public fun serialName(): String = ElevationCosts.serializer().descriptor.getElementName(this.ordinal)
}
