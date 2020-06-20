package com.mycars.base.di.modules

import com.mycars.base.network.HeadersInterceptor
import com.mycars.base.network.ServerLogsInterceptor
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoSet
import okhttp3.Interceptor

@Module
abstract class BaseInterceptorModule {

    @Binds
    @Reusable
    @IntoSet
    abstract fun bindServerLogsInterceptor(interceptor: ServerLogsInterceptor): Interceptor

    @Binds
    @Reusable
    @IntoSet
    abstract fun bindHeadersInterceptor(interceptor: HeadersInterceptor): Interceptor
}
