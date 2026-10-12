package org.litote.motis.client.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class Reachable(
  public val all: List<ReachablePlace>? = null,
  public val one: Place? = null,
)
