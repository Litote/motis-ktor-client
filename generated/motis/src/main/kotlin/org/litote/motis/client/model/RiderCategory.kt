package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class RiderCategory(
  public val eligibilityUrl: String? = null,
  public val isDefaultFareCategory: Boolean,
  public val riderCategoryName: String,
)
