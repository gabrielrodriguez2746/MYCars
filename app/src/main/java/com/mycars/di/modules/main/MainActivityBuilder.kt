package com.mycars.di.modules.main

import com.mycars.activities.MainActivity
import com.mycars.network.di.modules.CarsRepositoryModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityBuilder {

    @ContributesAndroidInjector(
        modules = [
            CarListFragmentBuilder::class,
            CarsRepositoryModule::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity
}