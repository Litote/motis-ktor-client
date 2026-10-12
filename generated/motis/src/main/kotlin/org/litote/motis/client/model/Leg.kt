package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.Serializable
import org.litote.motis.client.client.DateTimeFormatSerializer

@Serializable
public data class Leg(
  public val agencyId: String? = null,
  public val agencyName: String? = null,
  public val agencyUrl: String? = null,
  public val alerts: List<Alert>? = null,
  public val alternatives: List<List<Leg>>? = null,
  public val bikesAllowed: Boolean? = null,
  public val cancelled: Boolean? = null,
  public val category: Category? = null,
  public val directionId: String? = null,
  public val displayName: String? = null,
  public val distance: Double? = null,
  public val duration: Long,
  public val effectiveFareLegIndex: Long? = null,
  public val endTime: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val fareTransferIndex: Long? = null,
  public val from: Place,
  public val headsign: String? = null,
  public val interlineWithPreviousLeg: Boolean? = null,
  public val intermediateStops: List<Place>? = null,
  public val legGeometry: EncodedPolyline,
  public val loopedCalendarSince:
      @Serializable(with = DateTimeFormatSerializer::class) Instant? = null,
  public val mode: Mode = Mode.UNKNOWN_,
  public val realTime: Boolean,
  public val rental: Rental? = null,
  public val routeColor: String? = null,
  public val routeId: String? = null,
  public val routeLongName: String? = null,
  public val routeShortName: String? = null,
  public val routeTextColor: String? = null,
  public val routeType: Long? = null,
  public val routeUrl: String? = null,
  public val scheduled: Boolean,
  public val scheduledEndTime: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val scheduledStartTime: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val source: String? = null,
  public val startTime: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val steps: List<StepInstruction>? = null,
  public val to: Place,
  public val tripFrom: Place? = null,
  public val tripId: String? = null,
  public val tripShortName: String? = null,
  public val tripTo: Place? = null,
  public val wheelchairAccessible: WheelchairAccessibility? = null,
)
