package com.mycars.base.di.modules

import com.mycars.base.R
import com.mycars.base.providers.ResourceProvider
import com.mycars.base.rest.RxErrorHandlingCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.CallAdapter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Module
object CallAdapterFactoryModule {

    @Provides
    @Reusable
    @JvmStatic
    fun provideRxErrorHandlingCallAdapterFactory(provider: ResourceProvider): CallAdapter.Factory {
        return RxErrorHandlingCallAdapterFactory(
            provider.getString(R.string.base_generic_error),
            provider.getString(R.string.base_network_error),
            RxJava2CallAdapterFactory.create()
        )
    }

}