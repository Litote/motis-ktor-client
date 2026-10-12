package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class RefreshItineraryPostBody(
  public val detailedLegs: Boolean? = true,
  public val detailedTransfers: Boolean? = null,
  public val id: ItineraryId,
  public val joinInterlinedLegs: Boolean? = true,
  public val language: List<String>? = null,
  public val numLegAlternatives: Long? = 0,
  public val pedestrianProfile: PedestrianProfile? = null,
  public val requireBikeTransport: Boolean? = false,
  public val requireCarTransport: Boolean? = false,
  public val requireDisplayNameMatch: Boolean? = true,
  public val transitModes: List<Mode>? = null,
  public val useRoutedTransfers: Boolean? = false,
  public val withFares: Boolean? = false,
  public val withScheduledSkippedStops: Boolean? = false,
)
