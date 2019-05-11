package com.mycars.carsdetail.di.modules

import androidx.lifecycle.ViewModel
import com.mycars.base.di.ViewModelKey
import com.mycars.carsdetail.viewModels.CarDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CarDetailViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CarDetailViewModel::class)
    abstract fun bindCarDetailViewModel(viewModel: CarDetailViewModel): ViewModel

}