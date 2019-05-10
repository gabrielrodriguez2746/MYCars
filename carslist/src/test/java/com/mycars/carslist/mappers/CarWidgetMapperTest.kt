package com.mycars.carslist.mappers

import com.mycars.base.providers.ResourceProvider
import com.mycars.carslist.R
import com.mycars.carslist.models.CarWidgetItem
import com.mycars.data.models.cars.Car
import com.mycars.data.models.cars.Coordinate
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.tables.row
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CarWidgetMapperTest {

    private val resourceProvider: ResourceProvider = mockk()

    lateinit var mapper: CarWidgetMapper

    @BeforeEach
    fun beforeEach() {
        mapper = spyk(CarWidgetMapper(resourceProvider))
    }

    @Test
    fun `get from element`() {
        val expectedImageResource = 1

        val outputType = "Type POOLING"
        val outputCoordinates = "coordinates"
        val outputHeading = "Heading"

        val inputCar = getInputCar()
        val output = CarWidgetItem(439670, expectedImageResource, outputType, outputCoordinates, outputHeading)

        every { mapper.getImageFromType(inputCar.type) } returns expectedImageResource
        every { resourceProvider.getString(any(), inputCar.type) } returns outputType

        every {
            resourceProvider.getString(
                any(), inputCar.coordinate.latitude, inputCar.coordinate.longitude
            )
        } returns outputCoordinates

        every { resourceProvider.getString(any(), inputCar.heading) } returns outputHeading

        mapper.getFromElement(inputCar) shouldBe output
    }

    private fun getInputCar(): Car {
        return Car(
            439670,
            "POOLING",
            344.19529122029735,
            Coordinate(53.46036882190762, 9.909716434648558)
        )
    }

    @Test
    fun `get image from type`() {
        forall(
            row("POOLING", R.drawable.ic_car),
            row("TAXI", R.drawable.ic_taxi),
            row("anything", R.drawable.ic_taxi)
        ) { type, output ->
            mapper.getImageFromType(type) shouldBe output
        }
    }

}