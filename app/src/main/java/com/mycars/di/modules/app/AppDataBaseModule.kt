package com.mycars.di.modules.app

import com.mycars.carsdata.di.module.CarDataBaseModule
import dagger.Module

@Module(
    includes = [
        CarDataBaseModule::class
    ]
)
abstract class AppDataBaseModule