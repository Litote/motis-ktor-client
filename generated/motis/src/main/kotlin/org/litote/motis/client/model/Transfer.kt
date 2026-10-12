package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlinx.serialization.Serializable

@Serializable
public data class Transfer(
  public val car: Double? = null,
  public val default: Double? = null,
  public val foot: Double? = null,
  public val footRouted: Double? = null,
  public val to: Place,
  public val wheelchair: Double? = null,
  public val wheelchairRouted: Double? = null,
  public val wheelchairUsesElevator: Boolean? = null,
)
