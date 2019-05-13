package com.mycars.base

import android.app.Activity
import android.app.Application
import com.mycars.base.initializers.Initializer
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

abstract class InjectableApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var initializerSet: @JvmSuppressWildcards Set<Initializer>

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

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