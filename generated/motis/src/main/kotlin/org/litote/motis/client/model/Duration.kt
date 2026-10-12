package org.litote.motis.client.model

import kotlin.Double
import kotlinx.serialization.Serializable

@Serializable
public data class Duration(
  public val distance: Double? = null,
  public val duration: Double? = null,
)
