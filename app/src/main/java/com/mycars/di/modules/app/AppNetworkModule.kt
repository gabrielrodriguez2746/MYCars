package com.mycars.di.modules.app

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mycars.base.di.modules.BaseInterceptorModule
import com.mycars.base.di.modules.CallAdapterFactoryModule
import com.mycars.carsdata.deserializers.CarWrapperDeserializer
import com.mycars.carsdata.models.cars.CarWrapper
import com.mycars.network.di.modules.CarsClientModule
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module(
    includes = [
        BaseInterceptorModule::class,
        CallAdapterFactoryModule::class,
        CarsClientModule::class
    ]
)
object AppNetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    fun provideGson(): Gson {
        return with(GsonBuilder()) {
            registerTypeAdapter(CarWrapper::class.java, CarWrapperDeserializer())
            create()
        }
    }
}
