package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class AlertCause {
  @SerialName("UNKNOWN_CAUSE")
  UNKNOWN_CAUSE,
  @SerialName("OTHER_CAUSE")
  OTHER_CAUSE,
  @SerialName("TECHNICAL_PROBLEM")
  TECHNICAL_PROBLEM,
  @SerialName("STRIKE")
  STRIKE,
  @SerialName("DEMONSTRATION")
  DEMONSTRATION,
  @SerialName("ACCIDENT")
  ACCIDENT,
  @SerialName("HOLIDAY")
  HOLIDAY,
  @SerialName("WEATHER")
  WEATHER,
  @SerialName("MAINTENANCE")
  MAINTENANCE,
  @SerialName("CONSTRUCTION")
  CONSTRUCTION,
  @SerialName("POLICE_ACTIVITY")
  POLICE_ACTIVITY,
  @SerialName("MEDICAL_EMERGENCY")
  MEDICAL_EMERGENCY,
  @SerialName("SPECIAL_EVENT")
  SPECIAL_EVENT,
  UNKNOWN_,
  ;

  public fun serialName(): String = AlertCause.serializer().descriptor.getElementName(this.ordinal)
}
