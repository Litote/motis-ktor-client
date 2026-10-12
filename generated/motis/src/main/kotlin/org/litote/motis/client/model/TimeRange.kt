package org.litote.motis.client.model

import kotlin.time.Instant
import kotlinx.serialization.Serializable
import org.litote.motis.client.client.DateTimeFormatSerializer

@Serializable
public data class TimeRange(
  public val end: @Serializable(with = DateTimeFormatSerializer::class) Instant,
  public val start: @Serializable(with = DateTimeFormatSerializer::class) Instant,
)
