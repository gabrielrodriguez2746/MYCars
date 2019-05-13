package com.mycars.di.modules.main

import com.mycars.base.di.FragmentScope
import com.mycars.carshome.di.modules.CarMapViewModelModule
import com.mycars.carshome.fragments.CarMapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CarMapsFragmentBuilder {

    @ContributesAndroidInjector(
        modules = [CarMapViewModelModule::class]
    )
    @FragmentScope
    abstract fun bindCarMapFragment(): CarMapFragment
}