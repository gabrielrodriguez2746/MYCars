package com.mycars.network.di.modules

import com.google.gson.Gson
import com.mycars.base.providers.ResourceProvider
import com.mycars.network.R
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object CarsClientModule {

    @Provides
    @Reusable
    @JvmStatic
    fun provideCarsClient(
        interceptors: @JvmSuppressWildcards Set<Interceptor>
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            readTimeout(5, TimeUnit.SECONDS)
            connectTimeout(5, TimeUnit.SECONDS)
            interceptors.forEach { interceptor ->
                addInterceptor(interceptor)
            }
        }.build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideCarsService(
        httpClient: OkHttpClient,
        resourceProvider: ResourceProvider,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(resourceProvider.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

}