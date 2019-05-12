package com.mycars.carshome.di.modules

import androidx.lifecycle.ViewModel
import com.mycars.base.di.ViewModelKey
import com.mycars.base.mappers.BaseMapper
import com.mycars.carshome.mappers.CarWidgetMapper
import com.mycars.carshome.models.CarWidgetItem
import com.mycars.carshome.viewModels.CarListViewModel
import com.mycars.carsdata.models.cars.Car
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
    abstract fun bindCarWidgetMapper(mapper: CarWidgetMapper): BaseMapper<Car, CarWidgetItem>
}