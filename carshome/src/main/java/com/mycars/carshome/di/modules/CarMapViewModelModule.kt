package com.mycars.carshome.di.modules

import androidx.lifecycle.ViewModel
import com.mycars.base.di.ViewModelKey
import com.mycars.carshome.viewModels.CarMapsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CarMapViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CarMapsViewModel::class)
    abstract fun bindCarMapsViewModel(viewModel: CarMapsViewModel): ViewModel

}