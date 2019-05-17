package com.mycars.network.rest

import com.mycars.carsdata.models.cars.CarWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CarsService {

    @GET(".")
    fun getCarWrapper(
        @Query("p1Lat") p1Lat: Double = P1_LATITUDE,
        @Query("p1Lon") p1Lon: Double = P1_LONGITUDE,
        @Query("p2Lat") p2Lat: Double = P2_LATITUDE,
        @Query("p2Lon") p2Lon: Double = P2_LONGITUDE
    ): Single<CarWrapper>

    companion object {
        private const val P1_LATITUDE = 53.694865
        private const val P1_LONGITUDE = 9.757589
        private const val P2_LATITUDE = 53.394655
        private const val P2_LONGITUDE = 10.099891
    }
}
