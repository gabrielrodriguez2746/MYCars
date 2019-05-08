package com.mycars.base.di.modules

import androidx.lifecycle.ViewModelProvider
import com.mycars.base.factory.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class FactoryModule {

  @Binds
  abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}