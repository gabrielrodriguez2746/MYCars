package com.mycars.di.modules.app

import com.mycars.di.modules.launcher.LauncherActivityBuilder
import dagger.Module

@Module(includes = [LauncherActivityBuilder::class])
abstract class AppActivityBuilder