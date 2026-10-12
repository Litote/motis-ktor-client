package org.litote.motis.client.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.Serializable

@Serializable
public data class RentalStation(
  public val address: String? = null,
  public val bbox: List<Double>,
  public val crossStreet: String? = null,
  public val formFactors: List<RentalFormFactor>,
  public val id: String,
  public val isRenting: Boolean,
  public val isReturning: Boolean,
  public val lat: Double,
  public val lon: Double,
  public val name: String,
  public val numVehiclesAvailable: Long,
  public val providerGroupId: String,
  public val providerId: String,
  public val rentalUriAndroid: String? = null,
  public val rentalUriIOS: String? = null,
  public val rentalUriWeb: String? = null,
  public val stationArea: List<List<EncodedPolyline>>? = null,
  public val vehicleDocksAvailable: Map<String, Long>,
  public val vehicleTypesAvailable: Map<String, Long>,
)
