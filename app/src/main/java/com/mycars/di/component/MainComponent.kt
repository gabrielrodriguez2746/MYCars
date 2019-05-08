package com.mycars.di.component

import com.mycars.base.InjectableApplication
import com.mycars.base.di.component.BaseComponent
import com.mycars.base.di.modules.AppModule
import com.mycars.di.modules.AppConfigurationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppConfigurationModule::class,
    AppModule::class
])
interface MainComponent : BaseComponent {

    @Component.Builder
    interface Builder {

        fun build(): BaseComponent

        @BindsInstance
        fun application(application: InjectableApplication): Builder
    }
}