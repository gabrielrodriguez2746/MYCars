package com.mycars.base.di.modules

import com.mycars.base.config.ApplicationConfiguration
import com.mycars.base.config.BaseConfiguration
import com.mycars.base.navigation.ActivityNavigationProvider
import com.mycars.base.navigation.NavigationProvider
import com.mycars.base.providers.ApplicationResourceProvider
import com.mycars.base.providers.ResourceProvider
import dagger.Binds
import dagger.Module

@Module
abstract class CoreModule {

    @Binds
    abstract fun bindAppConfiguration(configuration: ApplicationConfiguration): BaseConfiguration

    @Binds
    abstract fun bindNavigationProvider(navigationProvider: ActivityNavigationProvider): NavigationProvider

    @Binds
    abstract fun bindResourceProvider(resourceProvider: ApplicationResourceProvider): ResourceProvider
}