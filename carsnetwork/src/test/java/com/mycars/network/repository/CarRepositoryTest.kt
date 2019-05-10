package com.mycars.network.repository

import com.mycars.data.models.cars.Car
import com.mycars.data.models.cars.CarWrapper
import com.mycars.network.rest.CarsService
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
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

    @Nested
    inner class `get single list data` {

        @Test
        fun success() {
            val output = listOf<Car>()
            every { service.getCarWrapper() } returns Single.just(CarWrapper(output))
            repository.getSingleListData(null)
                .test()
                .assertValue { it == output }
        }

        @Test
        fun error() {
            val output = Throwable()
            every { service.getCarWrapper() } returns Single.error(output)
            repository.getSingleListData(null)
                .test()
                .assertError(output)
        }
    }

    @AfterAll
    fun afterAll() {
        RxJavaPlugins.reset()
    }

}