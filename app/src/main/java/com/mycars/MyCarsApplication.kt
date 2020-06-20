package com.mycars

import com.mycars.base.InjectableApplication
import com.mycars.di.component.DaggerMainComponent

class MyCarsApplication : InjectableApplication() {

    override fun initializeInjector() {
        DaggerMainComponent.factory().create(this)
            .apply {
                inject(this@MyCarsApplication)
            }
    }
}
