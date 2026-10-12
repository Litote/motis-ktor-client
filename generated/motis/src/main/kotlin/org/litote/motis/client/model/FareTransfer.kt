package org.litote.motis.client.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class FareTransfer(
  public val effectiveFareLegProducts: List<List<List<FareProduct>>>,
  public val rule: FareTransferRule? = null,
  public val transferProducts: List<FareProduct>? = null,
)
