package com.mycars

import com.mycars.base.InjectableApplication
import com.mycars.di.component.DaggerMainComponent

class MyCarsApplication : InjectableApplication() {

    companion object {
        lateinit var instance: MyCarsApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun initializeInjector() {
        DaggerMainComponent.builder().application(this)
            .build()
            .apply {
                inject(this@MyCarsApplication)
            }
    }
}