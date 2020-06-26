package com.mycars.di.modules.launcher

import com.mycars.launcher.activities.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LauncherModule {

    @ContributesAndroidInjector
    abstract fun bindLauncherActivity(): SplashActivity
}
