package org.litote.motis.client.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class ItineraryId(
  public val legs: List<LegId>,
)
