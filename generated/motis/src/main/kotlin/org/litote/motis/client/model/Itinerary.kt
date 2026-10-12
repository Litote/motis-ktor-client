package org.litote.motis.client.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.Serializable
import org.litote.motis.client.client.DateTimeFormatSerializer

@Serializable
public data class Itinerary(
  public val duration: Long,
  public val endTime: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val fareTransfers: List<FareTransfer>? = null,
  public val id: String,
  public val legs: List<Leg>,
  public val startTime: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val transfers: Long,
)
