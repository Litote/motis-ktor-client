package org.litote.motis.client.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class FareProduct(
  public val amount: Double,
  public val currency: String,
  public val media: FareMedia? = null,
  public val name: String,
  public val riderCategory: RiderCategory? = null,
)
