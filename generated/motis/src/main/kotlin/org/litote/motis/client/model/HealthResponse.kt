package org.litote.motis.client.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

@Serializable
public data class HealthResponse(
  public val gbfs: Boolean? = null,
  public val rt: Boolean? = null,
)
