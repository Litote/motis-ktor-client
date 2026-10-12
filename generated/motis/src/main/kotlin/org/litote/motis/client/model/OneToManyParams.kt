package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class OneToManyParams(
  public val arriveBy: Boolean,
  public val elevationCosts: ElevationCosts? = null,
  public val many: List<String>,
  public val max: Double,
  public val maxMatchingDistance: Double,
  public val mode: Mode = Mode.UNKNOWN_,
  public val one: String,
  public val withDistance: Boolean? = false,
)
