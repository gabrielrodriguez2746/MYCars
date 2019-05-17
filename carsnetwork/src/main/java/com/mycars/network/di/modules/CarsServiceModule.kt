package com.mycars.network.di.modules

import com.mycars.network.rest.CarsService
import com.mycars.network.rest.MYCarsService
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class CarsServiceModule {

    @Binds
    @Reusable
    abstract fun bindPopularService(service: MYCarsService): CarsService
}
