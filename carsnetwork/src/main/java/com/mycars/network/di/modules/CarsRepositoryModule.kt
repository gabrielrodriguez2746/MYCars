package com.mycars.network.di.modules

import com.mycars.base.repository.BaseRepository
import com.mycars.data.models.cars.Car
import com.mycars.network.repository.CarRepository
import dagger.Binds
import dagger.Module

@Module(includes = [CarsServiceModule::class])
abstract class CarsRepositoryModule {

    @Binds
    abstract fun bindRepository(repository: CarRepository): BaseRepository<Any, Car>
}