package com.mycars.carsdata.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mycars.carsdata.dao.CarsDao
import com.mycars.carsdata.models.cars.Car

@Database(
    entities = [
        Car::class
    ], version = 1
)
abstract class CarsDatabase : RoomDatabase() {

    abstract fun carDatabase(): CarsDao
}
