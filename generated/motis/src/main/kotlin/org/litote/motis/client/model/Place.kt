package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.Serializable
import org.litote.motis.client.client.DateTimeFormatSerializer

@Serializable
public data class Place(
  public val alerts: List<Alert>? = null,
  public val arrival: @Serializable(with = DateTimeFormatSerializer::class) Instant? = null,
  public val cancelled: Boolean? = null,
  public val departure: @Serializable(with = DateTimeFormatSerializer::class) Instant? = null,
  public val description: String? = null,
  public val dropoffType: PickupDropoffType? = null,
  public val flex: String? = null,
  public val flexEndPickupDropOffWindow:
      @Serializable(with = DateTimeFormatSerializer::class) Instant? = null,
  public val flexId: String? = null,
  public val flexStartPickupDropOffWindow:
      @Serializable(with = DateTimeFormatSerializer::class) Instant? = null,
  public val importance: Double? = null,
  public val lat: Double,
  public val level: Double? = null,
  public val lon: Double,
  public val modes: List<Mode>? = null,
  public val name: String,
  public val parentId: String? = null,
  public val pickupType: PickupDropoffType? = null,
  public val scheduledArrival:
      @Serializable(with = DateTimeFormatSerializer::class) Instant? = null,
  public val scheduledDeparture:
      @Serializable(with = DateTimeFormatSerializer::class) Instant? = null,
  public val scheduledTrack: String? = null,
  public val stopCode: String? = null,
  public val stopId: String? = null,
  public val track: String? = null,
  public val tz: String? = null,
  public val vertexType: VertexType? = null,
)
