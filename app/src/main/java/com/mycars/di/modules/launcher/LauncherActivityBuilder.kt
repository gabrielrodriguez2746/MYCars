package com.mycars.di.modules.launcher

import androidx.appcompat.app.AppCompatActivity
import com.mycars.activities.MainActivity
import com.mycars.base.intents.ActivityClassIntent
import com.mycars.launcher.activities.SplashActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Inject
import kotlin.reflect.KClass

@Module
abstract class LauncherActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindLauncherActivity(): SplashActivity

    @Binds
    abstract fun bindActivityClassIntent(activityClassIntent: MainActivityClass): ActivityClassIntent
}

class MainActivityClass @Inject constructor() : ActivityClassIntent() {
    override fun getClassIntent(): @JvmSuppressWildcards KClass<out AppCompatActivity> = MainActivity::class
}