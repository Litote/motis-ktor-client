package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class FareTransferRule {
  @SerialName("A_AB")
  A_AB,
  @SerialName("A_AB_B")
  A_AB_B,
  @SerialName("AB")
  AB,
  UNKNOWN_,
  ;

  public fun serialName(): String = FareTransferRule.serializer().descriptor.getElementName(this.ordinal)
}
