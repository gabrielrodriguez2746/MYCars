package com.mycars.carshome.models

import androidx.annotation.DrawableRes

data class CarWidgetItem(
    val id: Int,
    @DrawableRes val imageType: Int,
    val type: String,
    val coordinates: String,
    val heading: String
)
