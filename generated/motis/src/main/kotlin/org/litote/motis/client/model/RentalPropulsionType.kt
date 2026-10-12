package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class RentalPropulsionType {
  @SerialName("HUMAN")
  HUMAN,
  @SerialName("ELECTRIC_ASSIST")
  ELECTRIC_ASSIST,
  @SerialName("ELECTRIC")
  ELECTRIC,
  @SerialName("COMBUSTION")
  COMBUSTION,
  @SerialName("COMBUSTION_DIESEL")
  COMBUSTION_DIESEL,
  @SerialName("HYBRID")
  HYBRID,
  @SerialName("PLUG_IN_HYBRID")
  PLUG_IN_HYBRID,
  @SerialName("HYDROGEN_FUEL_CELL")
  HYDROGEN_FUEL_CELL,
  UNKNOWN_,
  ;

  public fun serialName(): String = RentalPropulsionType.serializer().descriptor.getElementName(this.ordinal)
}
