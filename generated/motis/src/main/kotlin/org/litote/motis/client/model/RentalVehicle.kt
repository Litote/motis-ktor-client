package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class RentalVehicle(
  public val formFactor: RentalFormFactor = RentalFormFactor.UNKNOWN_,
  public val homeStationId: String? = null,
  public val id: String,
  public val isDisabled: Boolean,
  public val isReserved: Boolean,
  public val lat: Double,
  public val lon: Double,
  public val propulsionType: RentalPropulsionType = RentalPropulsionType.UNKNOWN_,
  public val providerGroupId: String,
  public val providerId: String,
  public val rentalUriAndroid: String? = null,
  public val rentalUriIOS: String? = null,
  public val rentalUriWeb: String? = null,
  public val returnConstraint: RentalReturnConstraint = RentalReturnConstraint.UNKNOWN_,
  public val stationId: String? = null,
  public val typeId: String,
)
