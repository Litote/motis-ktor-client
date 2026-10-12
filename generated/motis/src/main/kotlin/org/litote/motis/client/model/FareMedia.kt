package org.litote.motis.client.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class FareMedia(
  public val fareMediaName: String? = null,
  public val fareMediaType: FareMediaType = FareMediaType.UNKNOWN_,
)
