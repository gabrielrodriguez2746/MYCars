package com.mycars.base.di.modules

import android.app.Application
import android.content.Context
import com.mycars.base.InjectableApplication
import dagger.Module
import dagger.Provides

@Module(includes = [CoreModule::class, FactoryModule::class])
object CoreApplicationModule {

    @Provides
    fun providesContext(app: InjectableApplication): Context = app.applicationContext

    @Provides
    fun providesApplication(app: InjectableApplication): Application = app
}
