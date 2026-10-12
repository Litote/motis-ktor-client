package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class StopTime(
  public val agencyId: String,
  public val agencyName: String,
  public val agencyUrl: String,
  public val cancelled: Boolean,
  public val directionId: String,
  public val displayName: String,
  public val headsign: String,
  public val mode: Mode = Mode.UNKNOWN_,
  public val nextStops: List<Place>? = null,
  public val pickupDropoffType: PickupDropoffType = PickupDropoffType.UNKNOWN_,
  public val place: Place,
  public val previousStops: List<Place>? = null,
  public val realTime: Boolean,
  public val routeColor: String? = null,
  public val routeId: String,
  public val routeLongName: String,
  public val routeShortName: String,
  public val routeTextColor: String? = null,
  public val routeType: Long? = null,
  public val routeUrl: String? = null,
  public val source: String,
  public val tripCancelled: Boolean,
  public val tripFrom: Place,
  public val tripId: String,
  public val tripShortName: String,
  public val tripTo: Place,
)
