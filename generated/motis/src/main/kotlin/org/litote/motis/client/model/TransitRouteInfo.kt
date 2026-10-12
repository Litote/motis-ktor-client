package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class TransitRouteInfo(
  public val color: String? = null,
  public val id: String,
  public val longName: String,
  public val shortName: String,
  public val textColor: String? = null,
)
