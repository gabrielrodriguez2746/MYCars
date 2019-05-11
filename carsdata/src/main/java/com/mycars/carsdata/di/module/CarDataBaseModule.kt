package com.mycars.carsdata.di.module

import android.content.Context
import androidx.room.Room
import com.mycars.carsdata.database.CarsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object CarDataBaseModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideCarsDataBase(context: Context): CarsDatabase {
        return Room.databaseBuilder(context, CarsDatabase::class.java, "Cars.db")
            .fallbackToDestructiveMigration()
            .build()
    }


}