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

    @AfterAll
    fun afterAll() {
        RxJavaPlugins.reset()
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

            @Nested
            inner class `server response` {

                @Test
                fun success() {
                    val output = listOf<Car>()
                    every { repository.getDataFromServer() } returns Single.just(output)
                    repository.getSingleListData(null)
                        .test()
                        .assertValue(output)
                }

                @Test
                fun error() {
                    val output = Throwable()
                    every { repository.getDataFromServer() } returns Single.error(output)
                    repository.getSingleListData(null)
                        .test()
                        .assertError(output)
                }
            }
        }

        @Nested
        inner class `with persistence` {

            @Test
            fun `not empty`() {
                val carsPersistence = listOf<Car>(mockk())
                every { daoPersistence.getCarsPersistenceList() } returns Maybe.just(carsPersistence)
                every { repository.validateEmptyPersistence(any()) } returns Single.just(carsPersistence)
                repository.getSingleListData(null)
                    .test()
                    .assertValue(carsPersistence)
            }

            @Test
            fun empty() {
                val output = listOf<Car>(mockk())
                every { daoPersistence.getCarsPersistenceList() } returns Maybe.just(emptyList())
                every { repository.validateEmptyPersistence(any()) } returns Single.just(output)
                repository.getSingleListData(null)
                    .test()
                    .assertValue(output)
            }
        }
    }

    @Nested
    inner class `validate empty persistence` {

        @Test
        fun `empty input`() {
            val output = listOf<Car>(mockk())
            every { repository.getDataFromServer() } returns Single.just(output)
            repository.validateEmptyPersistence(emptyList())
                .test()
                .assertValue(output)
        }

        @Test
        fun `value input`() {
            val input = listOf<Car>(mockk())
            repository.validateEmptyPersistence(input)
                .test()
                .assertValue(input)
        }
    }

    @Nested
    inner class `get data from server` {

        @Test
        fun success() {
            val output = listOf<Car>(mockk())
            every { daoPersistence.insert(any()) } just runs
            every { service.getCarWrapper() } returns Single.just(CarWrapper(output))

            repository.getDataFromServer()
                .test()
                .assertValue(output)
        }

        @Test
        fun error() {
            val outputError = Throwable()
            every { service.getCarWrapper() } returns Single.error(outputError)

            repository.getDataFromServer()
                .test()
                .assertError(outputError)
        }
    }
}
