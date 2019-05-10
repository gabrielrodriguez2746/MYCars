package com.mycars.data.deserializers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mycars.basetest.readJsonFile
import com.mycars.data.helpers.getInvalidDefaultCar
import com.mycars.data.models.cars.Car
import com.mycars.data.models.cars.CarWrapper
import com.mycars.data.models.cars.Coordinate
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.FileReader

class CarWrapperDeserializerTest {

    lateinit var deserializer: CarWrapperDeserializer

    @BeforeEach
    fun beforeEach() {
        deserializer = spyk(CarWrapperDeserializer())
    }

    @Nested
    inner class `map car item from element` {

        @Test
        fun `empty element`() {
            val jsonElement = JsonObject()
            deserializer.mapCarItemFromElement(jsonElement) shouldBe getInvalidDefaultCar()
        }

        @Test
        fun `valid element`() {
            val jsonElement = readJsonFile(DATA)
            with(deserializer.mapCarItemFromElement(jsonElement)) {
                id shouldBe 439670
                type shouldBe "POOLING"
                heading shouldBe 344.19529122029735
                coordinate.latitude shouldBe 53.46036882190762
                coordinate.longitude shouldBe 9.909716434648558
            }
        }
    }

    @Nested
    inner class `filter results` {

        private val inputItems = JsonArray().apply { add(JsonObject()) }
        private val input = JsonObject().apply { add("poiList", inputItems) }

        @Test
        fun `invalid results`() {

            every { deserializer.mapCarItemFromElement(any()) } returns getInvalidDefaultCar()
            deserializer.deserialize(input, mockk(), mockk()) shouldBe CarWrapper(emptyList())
        }

        @Test
        fun `valid results`() {
            val expected = Car(
                439670,
                "POOLING",
                344.19529122029735,
                Coordinate(53.46036882190762, 9.909716434648558)
            )

            every { deserializer.mapCarItemFromElement(any()) } returns expected

            deserializer.deserialize(input, mockk(), mockk()).cars shouldBe listOf(expected)
        }

    }

    companion object {
        private const val DATA = "data.json"
    }

}