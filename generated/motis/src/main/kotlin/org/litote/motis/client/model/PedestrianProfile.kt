package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class PedestrianProfile {
  @SerialName("FOOT")
  FOOT,
  @SerialName("WHEELCHAIR")
  WHEELCHAIR,
  UNKNOWN_,
  ;

  public fun serialName(): String = PedestrianProfile.serializer().descriptor.getElementName(this.ordinal)
}
