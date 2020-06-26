package com.mycars.di.modules.features

import com.mycars.di.modules.launcher.LauncherModule
import com.mycars.di.modules.main.CarsModule
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        LauncherModule::class,
        CarsModule::class
    ]
)
object FeaturesModule