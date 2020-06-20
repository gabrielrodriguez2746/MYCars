package com.mycars.carsdata.helpers

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.tables.row
import org.junit.jupiter.api.Test

class CarExtensionsTest {

    @Test
    fun `get invalid default car`() {
        with(getInvalidDefaultCar()) {
            id shouldBe -1
            type shouldBe ""
            heading shouldBe 0.0
            coordinate.latitude shouldBe 0.0
            coordinate.longitude shouldBe 0.0
        }
    }

    @Test
    fun `is valid`() {
        forall(
            row(getInvalidDefaultCar(), false),
            row(getInvalidDefaultCar().copy(id = 1), true)
        ) { car, output ->
            car.isCarValid() shouldBe output
        }
    }
}
