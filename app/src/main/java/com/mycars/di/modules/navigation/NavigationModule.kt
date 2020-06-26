package com.mycars.di.modules.navigation

import com.mycars.activities.MainActivity
import com.mycars.base.navigation.NavigationClassIntent
import com.mycars.base.navigation.NavigationKey
import com.mycars.base.navigation.NavigationType
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
object NavigationModule {

    @Provides
    @IntoMap
    @NavigationKey(NavigationType.HOME)
    fun provideHomeNavigation() = NavigationClassIntent(MainActivity::class)
}