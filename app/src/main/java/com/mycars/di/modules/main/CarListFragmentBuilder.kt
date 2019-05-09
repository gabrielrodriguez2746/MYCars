package com.mycars.di.modules.main

import com.mycars.carslist.di.modules.CarListViewModelModule
import com.mycars.carslist.fragments.CarListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CarListFragmentBuilder {

    @ContributesAndroidInjector(
        modules = [CarListViewModelModule::class]
    )
    abstract fun bindCarListFragment(): CarListFragment
}