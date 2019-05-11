package com.mycars.network.di.modules

import com.mycars.base.repository.BaseRepository
import com.mycars.carsdata.dao.CarsDao
import com.mycars.carsdata.database.CarsDatabase
import com.mycars.carsdata.models.cars.Car
import com.mycars.network.repository.CarRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [CarsServiceModule::class])
abstract class CarsRepositoryModule {

    @Binds
    abstract fun bindRepository(repository: CarRepository): BaseRepository<Any, Car>

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideCarDao(database: CarsDatabase): CarsDao {
            return database.carDatabase()
        }

    }
}