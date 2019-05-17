package com.mycars.di.modules.main

import com.mycars.base.di.FragmentScope
import com.mycars.carshome.di.modules.CarListViewModelModule
import com.mycars.carshome.fragments.CarListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CarListFragmentBuilder {

    @ContributesAndroidInjector(
        modules = [CarListViewModelModule::class]
    )
    @FragmentScope
    abstract fun bindCarListFragment(): CarListFragment
}
