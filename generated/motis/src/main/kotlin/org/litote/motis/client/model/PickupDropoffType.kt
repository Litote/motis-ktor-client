package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class PickupDropoffType {
  @SerialName("NORMAL")
  NORMAL,
  @SerialName("NOT_ALLOWED")
  NOT_ALLOWED,
  UNKNOWN_,
  ;

  public fun serialName(): String = PickupDropoffType.serializer().descriptor.getElementName(this.ordinal)
}
