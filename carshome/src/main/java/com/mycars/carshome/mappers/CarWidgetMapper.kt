package com.mycars.carshome.mappers

import androidx.annotation.DrawableRes
import com.mycars.base.mappers.BaseMapper
import com.mycars.base.providers.ResourceProvider
import com.mycars.carshome.R
import com.mycars.carshome.models.CarWidgetItem
import com.mycars.carsdata.models.cars.Car
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
                resourceProvider.getString(R.string.carhome_coordinates_format, latitude, longitude),
                resourceProvider.getString(R.string.carhome_heading, heading)
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
