package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Error(
  public val error: String,
)
