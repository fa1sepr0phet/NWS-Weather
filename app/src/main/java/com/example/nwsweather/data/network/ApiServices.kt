package com.example.nwsweather.data.network

import com.example.nwsweather.data.model.NwsAlertsResponse
import com.example.nwsweather.data.model.NwsForecastResponse
import com.example.nwsweather.data.model.NwsPointsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface NwsApi {
    @GET("points/{lat},{lon}")
    suspend fun getPointMetadata(
        @Path("lat") lat: String,
        @Path("lon") lon: String
    ): NwsPointsResponse

    @GET
    suspend fun getForecast(@Url url: String): NwsForecastResponse

    @GET("alerts/active")
    suspend fun getActiveAlerts(
        @Query("point") point: String
    ): NwsAlertsResponse
}
