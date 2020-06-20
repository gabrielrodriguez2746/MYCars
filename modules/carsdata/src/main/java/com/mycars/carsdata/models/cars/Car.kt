package com.mycars.carsdata.models.cars

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car")
data class Car(
    @PrimaryKey
    @ColumnInfo(name = "car_id")
    val id: Int,
    val type: String,
    val heading: Double,
    @Embedded(prefix = "car_")
    val coordinate: Coordinate
)
