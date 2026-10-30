package com.example.bikekollectivefrontend

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Path

interface BikeApiService {

    @GET("data/bikes")
    suspend fun getBikes(): List<Bike>

    @POST("data/bikes")
    suspend fun addBike(@Body bike: NewBike): Bike

    @DELETE("data/bikes/{id}")
    suspend fun deleteBike(@Path("id") id: Int)
}

