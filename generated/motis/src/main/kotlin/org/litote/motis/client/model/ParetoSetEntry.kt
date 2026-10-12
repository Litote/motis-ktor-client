package org.litote.motis.client.model

import kotlin.Double
import kotlin.Long
import kotlinx.serialization.Serializable

@Serializable
public data class ParetoSetEntry(
  public val duration: Double,
  public val transfers: Long,
)
