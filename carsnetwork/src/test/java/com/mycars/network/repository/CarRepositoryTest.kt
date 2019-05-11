package com.mycars.network.repository

import com.mycars.carsdata.dao.CarsDao
import com.mycars.carsdata.models.cars.Car
import com.mycars.carsdata.models.cars.CarWrapper
import com.mycars.network.rest.CarsService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import io.reactivex.Maybe
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
    private val daoPersistence: CarsDao = mockk()

    lateinit var repository: CarRepository

    @BeforeEach
    fun beforeEach() {
        repository = spyk(CarRepository(service, daoPersistence))
    }

    @BeforeAll
    fun beforeAll() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @Nested
    inner class `get single list data` {

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        inner class `empty persistence` {

            @BeforeAll
            fun beforeAll() {
                every { daoPersistence.getCarsPersistenceList() } returns Maybe.empty()
            }

            @Test
            fun success() {
                val output = listOf<Car>()
                every { daoPersistence.insert(any()) } just runs
                every { service.getCarWrapper() } returns Single.just(CarWrapper(output))
                repository.getSingleListData(null)
                    .test()
                    .assertValue(output)
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

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        inner class `with persistence` {

            @Test
            fun `not empty`() {
                val carsPersistence = listOf<Car>(mockk())
                every { daoPersistence.getCarsPersistenceList() } returns Maybe.just(carsPersistence)
                repository.getSingleListData(null)
                    .test()
                    .assertValue(carsPersistence)
            }

            @Test
            fun empty() {
                val output = listOf<Car>(mockk())
                every { daoPersistence.getCarsPersistenceList() } returns Maybe.just(emptyList())
                every { repository.getDataFromServer() } returns Single.just(output)
                every { daoPersistence.insert(any()) } just runs
                repository.getSingleListData(null)
                    .test()
                    .assertValue(output)
            }
        }

    }

    @AfterAll
    fun afterAll() {
        RxJavaPlugins.reset()
    }

}