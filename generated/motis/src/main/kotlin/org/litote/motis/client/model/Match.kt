package org.litote.motis.client.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class Match(
  public val areas: List<Area>,
  public val category: String? = null,
  public val country: String? = null,
  public val houseNumber: String? = null,
  public val id: String,
  public val importance: Double? = null,
  public val lat: Double,
  public val level: Double? = null,
  public val lon: Double,
  public val modes: List<Mode>? = null,
  public val name: String,
  public val score: Double,
  public val street: String? = null,
  public val tokens: List<List<Double>>,
  public val type: LocationType = LocationType.UNKNOWN_,
  public val tz: String? = null,
  public val zip: String? = null,
)
