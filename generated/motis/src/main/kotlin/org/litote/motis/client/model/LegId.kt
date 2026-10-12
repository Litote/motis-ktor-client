package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class LegId(
  public val displayName: String,
  public val fromId: String,
  public val fromLat: Double,
  public val fromLevel: Double? = null,
  public val fromLon: Double,
  public val mode: Mode = Mode.UNKNOWN_,
  public val schedEnd: Long,
  public val schedStart: Long,
  public val scheduled: Boolean,
  public val toId: String,
  public val toLat: Double,
  public val toLevel: Double? = null,
  public val toLon: Double,
  public val tripId: String,
)
