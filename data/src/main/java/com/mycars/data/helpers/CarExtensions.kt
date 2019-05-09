@file:JvmName("CarExtensions")

package com.mycars.data.helpers

import com.mycars.data.models.cars.Car
import com.mycars.data.models.cars.Coordinate

fun getInvalidDefaultCar(): Car {
    return Car(id = -1, type = "", heading = 0.0, coordinate = Coordinate(0.0, 0.0))
}

fun Car.isCarValid(): Boolean {
    return id != -1
}