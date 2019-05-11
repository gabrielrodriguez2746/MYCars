package com.mycars.carslist.mappers

import androidx.annotation.DrawableRes
import com.mycars.base.mappers.BaseMapper
import com.mycars.base.providers.ResourceProvider
import com.mycars.carslist.R
import com.mycars.carslist.models.CarWidgetItem
import com.mycars.data.models.cars.Car
import javax.inject.Inject

class CarWidgetMapper @Inject constructor(
    private val resourceProvider: ResourceProvider
) : BaseMapper<Car, CarWidgetItem> {

    override fun getFromElement(element: Car): CarWidgetItem {
        return with(element) {
            val (latitude, longitude) = coordinate.latitude to coordinate.longitude
            CarWidgetItem(
                id, getImageFromType(type),
                type,
                resourceProvider.getString(R.string.carlist_coordinates_format, latitude, longitude),
                resourceProvider.getString(R.string.carlist_heading, heading)
            )
        }
    }

    @DrawableRes
    internal fun getImageFromType(type: String): Int {
        return if (type == "POOLING") {
            R.drawable.ic_car
        } else {
            R.drawable.ic_taxi
        }
    }
}