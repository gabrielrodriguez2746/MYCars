package com.mycars.data.deserializers

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.internal.LazilyParsedNumber
import com.mycars.base.helpers.getGenericOrDefault
import com.mycars.data.helpers.getInvalidDefaultCar
import com.mycars.data.helpers.isCarValid
import com.mycars.data.models.cars.Car
import com.mycars.data.models.cars.CarWrapper
import com.mycars.data.models.cars.Coordinate
import java.lang.reflect.Type
import javax.inject.Inject

class CarWrapperDeserializer @Inject constructor() : JsonDeserializer<CarWrapper> {

    // TODO this will also should include out of range value, that way default coordinate will be out of range
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): CarWrapper {

        val cars = json.asJsonObject.getGenericOrDefault("poiList", JsonArray())
            .map {
                mapCarItemFromElement(it.asJsonObject)
            }.filter {
                it.isCarValid()
            }

        return CarWrapper(cars)
    }

    internal fun mapCarItemFromElement(carObject: JsonObject): Car {
        val id = carObject.getGenericOrDefault("id", LazilyParsedNumber("-1")).toInt()
        return if (id == -1) {
            getInvalidDefaultCar()
        } else {
            val type = carObject.getGenericOrDefault("fleetType", "POOLING")
            val defaultDouble = LazilyParsedNumber("0.0")
            val heading = carObject.getGenericOrDefault("heading", defaultDouble).toDouble()
            val coordinateObject = carObject.getGenericOrDefault("coordinate", JsonObject())
            val (latitude, longitude) = with(coordinateObject) {
                getGenericOrDefault("latitude", defaultDouble) to getGenericOrDefault("longitude", defaultDouble)
            }
            Car(id = id, type = type, heading = heading, coordinate = Coordinate(latitude.toDouble(), longitude.toDouble()))
        }
    }

}