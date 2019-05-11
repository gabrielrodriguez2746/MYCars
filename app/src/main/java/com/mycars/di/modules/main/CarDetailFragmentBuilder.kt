package com.mycars.di.modules.main

import com.mycars.carsdetail.di.modules.CarDetailViewModelModule
import com.mycars.carsdetail.fragments.CarDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CarDetailFragmentBuilder {

    @ContributesAndroidInjector(
        modules = [CarDetailViewModelModule::class]
    )
    abstract fun bindCarDetailFragment(): CarDetailFragment
}