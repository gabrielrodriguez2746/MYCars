package com.mycars.di.modules.launcher

import com.mycars.base.intents.ActivityClassIntent
import com.mycars.intent.MainActivityClass
import com.mycars.launcher.activities.SplashActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LauncherActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindLauncherActivity(): SplashActivity

    @Binds
    abstract fun bindActivityClassIntent(activityClassIntent: MainActivityClass): ActivityClassIntent
}
