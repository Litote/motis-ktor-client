package org.litote.motis.client.model

import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class RouteInfo(
  public val mode: Mode = Mode.UNKNOWN_,
  public val numStops: Long,
  public val pathSource: RoutePathSource = RoutePathSource.UNKNOWN_,
  public val routeIdx: Long,
  public val segments: List<RouteSegment>,
  public val transitRoutes: List<TransitRouteInfo>,
)
