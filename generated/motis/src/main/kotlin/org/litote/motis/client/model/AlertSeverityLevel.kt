package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class AlertSeverityLevel {
  @SerialName("UNKNOWN_SEVERITY")
  UNKNOWN_SEVERITY,
  @SerialName("INFO")
  INFO,
  @SerialName("WARNING")
  WARNING,
  @SerialName("SEVERE")
  SEVERE,
  UNKNOWN_,
  ;

  public fun serialName(): String = AlertSeverityLevel.serializer().descriptor.getElementName(this.ordinal)
}
