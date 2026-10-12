package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class Mode {
  @SerialName("WALK")
  WALK,
  @SerialName("BIKE")
  BIKE,
  @SerialName("RENTAL")
  RENTAL,
  @SerialName("CAR")
  CAR,
  @SerialName("CAR_PARKING")
  CAR_PARKING,
  @SerialName("CAR_DROPOFF")
  CAR_DROPOFF,
  @SerialName("ODM")
  ODM,
  @SerialName("RIDE_SHARING")
  RIDE_SHARING,
  @SerialName("FLEX")
  FLEX,
  @SerialName("DEBUG_BUS_ROUTE")
  DEBUG_BUS_ROUTE,
  @SerialName("DEBUG_RAILWAY_ROUTE")
  DEBUG_RAILWAY_ROUTE,
  @SerialName("DEBUG_FERRY_ROUTE")
  DEBUG_FERRY_ROUTE,
  @SerialName("TRANSIT")
  TRANSIT,
  @SerialName("TRAM")
  TRAM,
  @SerialName("SUBWAY")
  SUBWAY,
  @SerialName("FERRY")
  FERRY,
  @SerialName("AIRPLANE")
  AIRPLANE,
  @SerialName("BUS")
  BUS,
  @SerialName("COACH")
  COACH,
  @SerialName("RAIL")
  RAIL,
  @SerialName("HIGHSPEED_RAIL")
  HIGHSPEED_RAIL,
  @SerialName("LONG_DISTANCE")
  LONG_DISTANCE,
  @SerialName("NIGHT_RAIL")
  NIGHT_RAIL,
  @SerialName("REGIONAL_FAST_RAIL")
  REGIONAL_FAST_RAIL,
  @SerialName("REGIONAL_RAIL")
  REGIONAL_RAIL,
  @SerialName("SUBURBAN")
  SUBURBAN,
  @SerialName("FUNICULAR")
  FUNICULAR,
  @SerialName("AERIAL_LIFT")
  AERIAL_LIFT,
  @SerialName("OTHER")
  OTHER,
  @SerialName("AREAL_LIFT")
  AREAL_LIFT,
  @SerialName("METRO")
  METRO,
  @SerialName("CABLE_CAR")
  CABLE_CAR,
  UNKNOWN_,
  ;

  public fun serialName(): String = Mode.serializer().descriptor.getElementName(this.ordinal)
}
