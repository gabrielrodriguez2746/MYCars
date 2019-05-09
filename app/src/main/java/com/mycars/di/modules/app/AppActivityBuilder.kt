package com.mycars.di.modules.app

import com.mycars.di.modules.launcher.LauncherActivityBuilder
import com.mycars.di.modules.main.MainActivityBuilder
import dagger.Module

@Module(includes = [LauncherActivityBuilder::class, MainActivityBuilder::class])
abstract class AppActivityBuilder