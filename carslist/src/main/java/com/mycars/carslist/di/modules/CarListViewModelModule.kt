package com.mycars.carslist.di.modules

import androidx.lifecycle.ViewModel
import com.mycars.base.di.ViewModelKey
import com.mycars.base.mappers.BaseMapper
import com.mycars.carslist.mappers.CarWidgetMapper
import com.mycars.carslist.models.CarWidgetItem
import com.mycars.carslist.viewModels.CarListViewModel
import com.mycars.data.models.cars.Car
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap

@Module
abstract class CarListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CarListViewModel::class)
    abstract fun bindCarListViewModel(viewModel: CarListViewModel): ViewModel

    @Binds
    @Reusable
    abstract fun bindCarWidgetMapper(mapper: CarWidgetMapper): BaseMapper<Car, CarWidgetItem>
}