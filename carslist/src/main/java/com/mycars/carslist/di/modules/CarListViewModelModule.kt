package com.mycars.carslist.di.modules

import androidx.lifecycle.ViewModel
import com.mycars.base.di.ViewModelKey
import com.mycars.carslist.viewModels.CarListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CarListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CarListViewModel::class)
    abstract fun bindCarListViewModel(viewModel: CarListViewModel): ViewModel
}