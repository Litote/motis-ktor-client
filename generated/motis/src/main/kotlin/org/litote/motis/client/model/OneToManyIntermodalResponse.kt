package org.litote.motis.client.model

import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class OneToManyIntermodalResponse(
  @SerialName("street_durations")
  public val streetDurations: List<Duration>? = null,
  @SerialName("transit_durations")
  public val transitDurations: List<List<ParetoSetEntry>>? = null,
)
