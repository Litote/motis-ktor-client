package org.litote.motis.client.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class RentalZone(
  public val area: List<List<EncodedPolyline>>,
  public val bbox: List<Double>,
  public val name: String? = null,
  public val providerGroupId: String,
  public val providerId: String,
  public val rules: List<RentalZoneRestrictions>,
  public val z: Long,
)
