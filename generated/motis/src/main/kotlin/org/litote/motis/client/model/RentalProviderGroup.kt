package org.litote.motis.client.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class RentalProviderGroup(
  public val color: String? = null,
  public val formFactors: List<RentalFormFactor>,
  public val id: String,
  public val name: String,
  public val providers: List<String>,
)
