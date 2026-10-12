package org.litote.motis.client.model

import kotlin.Long
import kotlinx.serialization.Serializable

@Serializable
public data class RouteSegment(
  public val from: Long,
  public val polyline: Long,
  public val to: Long,
)
