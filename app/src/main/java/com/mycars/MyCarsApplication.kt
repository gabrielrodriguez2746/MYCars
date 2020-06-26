package com.mycars

import com.mycars.base.InjectableApplication
import com.mycars.base.config.IsDebug.Companion.toIsDebug
import com.mycars.di.component.DaggerMainComponent

class MyCarsApplication : InjectableApplication() {

    override fun initializeInjector() {
        DaggerMainComponent.factory().create(this, BuildConfig.DEBUG.toIsDebug())
            .apply {
                inject(this@MyCarsApplication)
            }
    }
}
