package com.mycars.di.modules.app

import com.mycars.base.initializers.Initializer
import com.mycars.initializers.DataBaseInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class AppInitializerModule {

    @Binds
    @IntoSet
    abstract fun bindDataBaseInitializersModule(dataBaseInitializer: DataBaseInitializer): Initializer
}
