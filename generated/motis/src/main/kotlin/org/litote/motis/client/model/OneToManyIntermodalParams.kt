package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.Serializable
import org.litote.motis.client.client.DateTimeFormatSerializer

@Serializable
public data class OneToManyIntermodalParams(
  public val additionalTransferTime: Long? = 0,
  public val arriveBy: Boolean? = false,
  public val cyclingSpeed: Double? = null,
  public val directMode: Mode? = null,
  public val elevationCosts: ElevationCosts? = null,
  public val many: List<String>,
  public val maxDirectTime: Long? = 1800,
  public val maxMatchingDistance: Double? = 25.0,
  public val maxPostTransitTime: Long? = 900,
  public val maxPreTransitTime: Long? = 900,
  public val maxTransfers: Long? = null,
  public val maxTravelTime: Long? = null,
  public val minTransferTime: Long? = 0,
  public val one: String,
  public val pedestrianProfile: PedestrianProfile? = null,
  public val pedestrianSpeed: Double? = null,
  public val postTransitModes: List<Mode>? = null,
  public val preTransitModes: List<Mode>? = null,
  public val requireBikeTransport: Boolean? = false,
  public val requireCarTransport: Boolean? = false,
  public val time: @Serializable(with = DateTimeFormatSerializer::class) Instant? = null,
  public val transferTimeFactor: Double? = 1.0,
  public val transitModes: List<Mode>? = null,
  public val useRoutedTransfers: Boolean? = false,
  public val withDistance: Boolean? = false,
)
