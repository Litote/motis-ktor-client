package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Serializable
public enum class VertexType {
  @SerialName("NORMAL")
  NORMAL,
  @SerialName("BIKESHARE")
  BIKESHARE,
  @SerialName("TRANSIT")
  TRANSIT,
  UNKNOWN_,
  ;

  public fun serialName(): String = VertexType.serializer().descriptor.getElementName(this.ordinal)
}
