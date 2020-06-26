package com.mycars.di.modules.launcher

import com.mycars.activities.MainActivity
import com.mycars.base.navigation.NavigationClassIntent
import com.mycars.base.navigation.NavigationType
import com.mycars.base.navigation.NavigationKey
import com.mycars.launcher.activities.SplashActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [LauncherNavigationModule::class])
abstract class LauncherActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindLauncherActivity(): SplashActivity
}

@Module
object LauncherNavigationModule {

    @Provides
    @IntoMap
    @NavigationKey(NavigationType.HOME)
    fun provideHomeNavigation() = NavigationClassIntent(MainActivity::class)

}
