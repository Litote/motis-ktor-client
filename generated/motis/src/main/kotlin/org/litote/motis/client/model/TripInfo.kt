package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class TripInfo(
  public val displayName: String? = null,
  public val routeShortName: String? = null,
  public val tripId: String,
)
