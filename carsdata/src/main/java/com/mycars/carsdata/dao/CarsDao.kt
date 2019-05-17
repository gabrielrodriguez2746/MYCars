package com.mycars.carsdata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mycars.carsdata.models.cars.Car
import io.reactivex.Maybe

@Dao
abstract class CarsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(orders: List<Car>)

    @Query("SELECT * FROM car WHERE car_id = :carId")
    abstract fun getCarById(carId: Int): Maybe<Car>

    @Query("SELECT * FROM car")
    abstract fun getCarsPersistenceList(): Maybe<List<Car>>

    @Query("DELETE FROM car")
    abstract fun deleteCarsPersitence()
}
