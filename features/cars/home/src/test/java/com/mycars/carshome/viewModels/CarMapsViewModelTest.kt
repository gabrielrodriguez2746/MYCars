package com.mycars.carshome.viewModels

import com.mycars.base.repository.BaseRepository
import com.mycars.basetest.InstantExecutorExtension
import com.mycars.carsdata.models.cars.Car
import com.mycars.carsdata.models.cars.Coordinate
import com.mycars.carshome.viewModels.CarMapsViewModel.CarMapsViewModelEvents.OnEmptyResults
import com.mycars.carshome.viewModels.CarMapsViewModel.CarMapsViewModelEvents.OnRequestError
import com.mycars.carshome.viewModels.CarMapsViewModel.CarMapsViewModelEvents.OnMapItems
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarMapsViewModelTest {

    private val repository: BaseRepository<Any, Int, Car> = mockk()

    lateinit var viewModel: CarMapsViewModel

    @BeforeAll
    fun beforeAll() {
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @AfterAll
    fun afterAll() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @BeforeEach
    fun beforeEach() {
        viewModel = spyk(CarMapsViewModel(repository))
    }

    @Nested
    inner class `on create` {

        @Nested
        inner class `with data form repository` {

            @Test
            fun empty() {

                every { repository.getSingleListData(any()) } returns Single.just(emptyList())

                viewModel.onCreate()
                viewModel.events.value.shouldBeInstanceOf<OnEmptyResults>()
            }

            @Test
            fun items() {

                val serverItem = mockk<Car> {
                    every { type } returns ""
                    every { coordinate } returns Coordinate(0.0, 0.0)
                }
                every { repository.getSingleListData(any()) } returns Single.just(listOf(serverItem))

                viewModel.onCreate()
                viewModel.events.value.shouldBeInstanceOf<OnMapItems>()
            }
        }

        @Test
        fun `with error from repository`() {
            every { repository.getSingleListData(any()) } returns Single.error(Throwable())
            viewModel.onCreate()
            viewModel.events.value.shouldBeInstanceOf<OnRequestError>()
        }
    }

    @Test
    fun `on destroy`() {
        every { viewModel.dispose() } just runs
        viewModel.onDestroy()
        verify(exactly = 1) { viewModel.dispose() }
    }
}
