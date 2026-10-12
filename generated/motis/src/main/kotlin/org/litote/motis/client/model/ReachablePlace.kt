package org.litote.motis.client.model

import kotlin.Long
import kotlinx.serialization.Serializable

@Serializable
public data class ReachablePlace(
  public val duration: Long? = null,
  public val k: Long? = null,
  public val place: Place? = null,
)
