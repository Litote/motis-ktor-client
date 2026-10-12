package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class StepInstruction(
  public val accessRestriction: String? = null,
  public val area: Boolean,
  public val distance: Double,
  public val elevationDown: Long? = null,
  public val elevationUp: Long? = null,
  public val exit: String,
  public val fromLevel: Double,
  public val osmWay: Long? = null,
  public val polyline: EncodedPolyline,
  public val relativeDirection: Direction = Direction.UNKNOWN_,
  public val stayOn: Boolean,
  public val streetName: String,
  public val toLevel: Double,
  public val toll: Boolean? = null,
)
