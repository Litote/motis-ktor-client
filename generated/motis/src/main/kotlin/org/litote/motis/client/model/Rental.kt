package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Rental(
  public val color: String? = null,
  public val formFactor: RentalFormFactor? = null,
  public val fromStationName: String? = null,
  public val propulsionType: RentalPropulsionType? = null,
  public val providerGroupId: String,
  public val providerId: String,
  public val rentalUriAndroid: String? = null,
  public val rentalUriIOS: String? = null,
  public val rentalUriWeb: String? = null,
  public val returnConstraint: RentalReturnConstraint? = null,
  public val stationName: String? = null,
  public val systemId: String,
  public val systemName: String? = null,
  public val toStationName: String? = null,
  public val url: String? = null,
)
