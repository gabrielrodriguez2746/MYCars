package com.mycars.network.rest

import com.mycars.data.models.cars.CarWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CarsService {

    @GET(".")
    fun getCarWrapper(
        @Query("p1Lat") p1Lat: Double = 53.694865,
        @Query("p1Lon") p1Lon: Double = 9.757589,
        @Query("p2Lat") p2Lat: Double = 53.394655,
        @Query("p2Lon") p2Lon: Double = 10.099891
    ): Single<CarWrapper>
}