package com.mycars.data.models.cars

data class Car(
    val id: Int,
    val type: String,
    val heading: Double,
    val coordinate: Coordinate
)