package com.mycars.di.component

import com.mycars.base.InjectableApplication
import com.mycars.base.di.component.BaseComponent
import com.mycars.base.di.modules.AppModule
import com.mycars.di.modules.app.AppActivityBuilder
import com.mycars.di.modules.app.AppConfigurationModule
import com.mycars.di.modules.app.AppNetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppConfigurationModule::class,
    AppActivityBuilder::class,
    AppModule::class,
    AppNetworkModule::class
])
interface MainComponent : BaseComponent {

    @Component.Builder
    interface Builder {

        fun build(): BaseComponent

        @BindsInstance
        fun application(application: InjectableApplication): Builder
    }
}