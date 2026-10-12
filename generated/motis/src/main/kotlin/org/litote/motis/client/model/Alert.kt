package org.litote.motis.client.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class Alert(
  public val cause: AlertCause? = null,
  public val causeDetail: String? = null,
  public val code: String? = null,
  public val communicationPeriod: List<TimeRange>? = null,
  public val descriptionText: String,
  public val effect: AlertEffect? = null,
  public val effectDetail: String? = null,
  public val headerText: String,
  public val imageAlternativeText: String? = null,
  public val imageMediaType: String? = null,
  public val imageUrl: String? = null,
  public val impactPeriod: List<TimeRange>? = null,
  public val severityLevel: AlertSeverityLevel? = null,
  public val ttsDescriptionText: String? = null,
  public val ttsHeaderText: String? = null,
  public val url: String? = null,
)
