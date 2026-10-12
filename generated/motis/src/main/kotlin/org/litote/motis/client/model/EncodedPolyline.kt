package org.litote.motis.client.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class EncodedPolyline(
  public val length: Long,
  public val points: String,
  public val precision: Long,
)
