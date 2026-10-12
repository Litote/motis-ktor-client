package org.litote.motis.client.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class RentalProvider(
  public val bbox: List<Double>,
  public val color: String? = null,
  public val defaultRestrictions: RentalZoneRestrictions,
  public val formFactors: List<RentalFormFactor>,
  public val globalGeofencingRules: List<RentalZoneRestrictions>,
  public val groupId: String,
  public val id: String,
  public val name: String,
  public val `operator`: String? = null,
  public val purchaseUrl: String? = null,
  public val url: String? = null,
  public val vehicleTypes: List<RentalVehicleType>,
)
