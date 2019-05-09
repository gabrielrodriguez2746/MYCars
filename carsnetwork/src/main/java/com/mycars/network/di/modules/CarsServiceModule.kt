package com.mycars.network.di.modules

import com.mycars.network.di.rest.CarsService
import com.mycars.network.di.rest.MYCarsService
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class CarsServiceModule {

    @Binds
    @Reusable
    abstract fun bindPopularService(service: MYCarsService): CarsService

}