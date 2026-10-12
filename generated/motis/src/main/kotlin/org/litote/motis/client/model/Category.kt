package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Category(
  public val id: String,
  public val name: String,
  public val shortName: String,
)
