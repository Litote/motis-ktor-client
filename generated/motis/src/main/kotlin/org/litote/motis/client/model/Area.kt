package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Area(
  public val adminLevel: Double,
  public val default: Boolean? = null,
  public val matched: Boolean,
  public val name: String,
  public val unique: Boolean? = null,
)
