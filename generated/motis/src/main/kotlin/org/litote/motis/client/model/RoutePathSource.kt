package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class RoutePathSource {
  @SerialName("NONE")
  NONE,
  @SerialName("TIMETABLE")
  TIMETABLE,
  @SerialName("ROUTED")
  ROUTED,
  UNKNOWN_,
  ;

  public fun serialName(): String = RoutePathSource.serializer().descriptor.getElementName(this.ordinal)
}
