package com.mycars.network.rest

import retrofit2.Retrofit
import javax.inject.Inject

class MYCarsService @Inject constructor(retrofit: Retrofit) : CarsService by retrofit.create(CarsService::class.java)