package com.mycars.network.repository

import com.mycars.data.models.cars.Car
import com.mycars.data.models.cars.CarWrapper
import com.mycars.network.rest.CarsService
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarRepositoryTest {

    private val service: CarsService = mockk()

    lateinit var repository: CarRepository

    @BeforeEach
    fun beforeEach() {
        repository = spyk(CarRepository(service))
    }

    @BeforeAll
    fun beforeAll() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `get single list data`() {
        val output = listOf<Car>()
        every { service.getCarWrapper() } returns Single.just(CarWrapper(output))
        repository.getSingleListData(null)
            .test()
            .assertValue { it == output }
    }

    @AfterAll
    fun afterAll() {
        RxJavaPlugins.reset()
    }

}