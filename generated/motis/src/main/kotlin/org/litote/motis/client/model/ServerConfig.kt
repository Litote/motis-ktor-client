package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ServerConfig(
  public val hasElevation: Boolean,
  public val hasRoutedTransfers: Boolean,
  public val hasStreetRouting: Boolean,
  public val maxDirectTimeLimit: Double,
  public val maxOneToAllTravelTimeLimit: Double,
  public val maxOneToManySize: Double,
  public val maxPrePostTransitTimeLimit: Double,
  public val motisVersion: String,
  public val shapesDebugEnabled: Boolean,
)
