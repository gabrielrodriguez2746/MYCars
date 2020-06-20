package com.mycars.base

import android.app.Application
import com.mycars.base.initializers.Initializer
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class InjectableApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var initializerSet: @JvmSuppressWildcards Set<Initializer>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        initializeInjector()
        initializeClasses()
    }

    abstract fun initializeInjector()

    private fun initializeClasses() {
        initializerSet.forEach { it.init() }
    }
}
