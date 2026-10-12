package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class Direction {
  @SerialName("DEPART")
  DEPART,
  @SerialName("HARD_LEFT")
  HARD_LEFT,
  @SerialName("LEFT")
  LEFT,
  @SerialName("SLIGHTLY_LEFT")
  SLIGHTLY_LEFT,
  @SerialName("CONTINUE")
  CONTINUE,
  @SerialName("SLIGHTLY_RIGHT")
  SLIGHTLY_RIGHT,
  @SerialName("RIGHT")
  RIGHT,
  @SerialName("HARD_RIGHT")
  HARD_RIGHT,
  @SerialName("CIRCLE_CLOCKWISE")
  CIRCLE_CLOCKWISE,
  @SerialName("CIRCLE_COUNTERCLOCKWISE")
  CIRCLE_COUNTERCLOCKWISE,
  @SerialName("STAIRS")
  STAIRS,
  @SerialName("ELEVATOR")
  ELEVATOR,
  @SerialName("UTURN_LEFT")
  UTURN_LEFT,
  @SerialName("UTURN_RIGHT")
  UTURN_RIGHT,
  UNKNOWN_,
  ;

  public fun serialName(): String = Direction.serializer().descriptor.getElementName(this.ordinal)
}
