package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class AlertEffect {
  @SerialName("NO_SERVICE")
  NO_SERVICE,
  @SerialName("REDUCED_SERVICE")
  REDUCED_SERVICE,
  @SerialName("SIGNIFICANT_DELAYS")
  SIGNIFICANT_DELAYS,
  @SerialName("DETOUR")
  DETOUR,
  @SerialName("ADDITIONAL_SERVICE")
  ADDITIONAL_SERVICE,
  @SerialName("MODIFIED_SERVICE")
  MODIFIED_SERVICE,
  @SerialName("OTHER_EFFECT")
  OTHER_EFFECT,
  @SerialName("UNKNOWN_EFFECT")
  UNKNOWN_EFFECT,
  @SerialName("STOP_MOVED")
  STOP_MOVED,
  @SerialName("NO_EFFECT")
  NO_EFFECT,
  @SerialName("ACCESSIBILITY_ISSUE")
  ACCESSIBILITY_ISSUE,
  UNKNOWN_,
  ;

  public fun serialName(): String = AlertEffect.serializer().descriptor.getElementName(this.ordinal)
}
