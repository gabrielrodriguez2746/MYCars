package com.mycars.di.component

import com.mycars.base.InjectableApplication
import com.mycars.base.config.IsDebug
import com.mycars.base.di.component.BaseComponent
import com.mycars.base.di.modules.CoreApplicationModule
import com.mycars.di.modules.app.AppDataBaseModule
import com.mycars.di.modules.app.AppInitializerModule
import com.mycars.di.modules.app.AppNetworkModule
import com.mycars.di.modules.features.FeaturesModule
import com.mycars.di.modules.navigation.NavigationModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppInitializerModule::class,
        AppDataBaseModule::class,
        CoreApplicationModule::class,
        AppNetworkModule::class,
        NavigationModule::class,
        FeaturesModule::class
    ]
)
interface MainComponent : BaseComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: InjectableApplication,
            @BindsInstance IsDebug: IsDebug
        ): BaseComponent
    }
}
