package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class RentalZoneRestrictions(
  public val rideEndAllowed: Boolean,
  public val rideStartAllowed: Boolean,
  public val rideThroughAllowed: Boolean,
  public val stationParking: Boolean? = null,
  public val vehicleTypeIdxs: List<Long>,
)
