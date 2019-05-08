package com.mycars.di.modules.app

import com.mycars.base.config.BaseConfiguration
import com.mycars.base.providers.ResourceProvider
import com.mycars.config.AppConfiguration
import com.mycars.providers.AppResourceProvider
import dagger.Binds
import dagger.Module

@Module
abstract class AppConfigurationModule {

    @Binds
    abstract fun bindAppConfiguration(configuration: AppConfiguration): BaseConfiguration

    @Binds
    abstract fun bindResourceProvider(resourceProvider: AppResourceProvider): ResourceProvider
}