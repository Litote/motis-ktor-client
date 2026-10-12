package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class RentalVehicleType(
  public val formFactor: RentalFormFactor = RentalFormFactor.UNKNOWN_,
  public val id: String,
  public val name: String? = null,
  public val propulsionType: RentalPropulsionType = RentalPropulsionType.UNKNOWN_,
  public val returnConstraint: RentalReturnConstraint = RentalReturnConstraint.UNKNOWN_,
  public val returnConstraintGuessed: Boolean,
)
