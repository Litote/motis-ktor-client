package org.litote.motis.client.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class RoutePolyline(
  public val colors: List<String>,
  public val polyline: EncodedPolyline,
  public val routeIndexes: List<Long>,
)
