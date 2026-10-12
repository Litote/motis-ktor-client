package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.Serializable
import org.litote.motis.client.client.DateTimeFormatSerializer

@Serializable
public data class TripSegment(
  public val arrival: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val departure: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val distance: Double,
  public val from: Place,
  public val mode: Mode = Mode.UNKNOWN_,
  public val polyline: String,
  public val realTime: Boolean,
  public val routeColor: String? = null,
  public val scheduledArrival: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val scheduledDeparture: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val to: Place,
  public val trips: List<TripInfo>,
)
