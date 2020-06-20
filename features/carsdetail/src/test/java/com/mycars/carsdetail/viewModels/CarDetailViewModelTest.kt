package com.mycars.carsdetail.viewModels

import com.mycars.base.providers.ResourceProvider
import com.mycars.base.repository.BaseRepository
import com.mycars.basetest.InstantExecutorExtension
import com.mycars.carsdata.models.cars.Car
import com.mycars.carsdata.models.cars.Coordinate
import com.mycars.carsdetail.viewModels.CarDetailViewModel.CarDetailViewModelEvents.OnMapItems
import com.mycars.carsdetail.viewModels.CarDetailViewModel.CarDetailViewModelEvents.OnNotFoundCar
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
class CarDetailViewModelTest {

    private val resourceProvider: ResourceProvider = mockk()
    private val repository: BaseRepository<Any, Int, Car> = mockk()

    lateinit var viewModel: CarDetailViewModel

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
        viewModel = spyk(CarDetailViewModel(resourceProvider, repository))
    }

    @Nested
    inner class `locate car by id` {

        private val id = 32

        @Test
        fun `with data form repository`() {

            val latitude = 0.0
            val longitude = 0.0
            val serverItem = mockk<Car> {
                every { type } returns ""
                every { coordinate } returns Coordinate(latitude, longitude)
            }
            every { resourceProvider.getString(any(), latitude, longitude) } returns ""
            every { repository.getSingleDataByIdentifier(any()) } returns Single.just(serverItem)

            viewModel.locateCarById(id)
            viewModel.events.value.shouldBeInstanceOf<OnMapItems>()
        }

        @Test
        fun `with error from repository`() {
            every { repository.getSingleDataByIdentifier(any()) } returns Single.error(Throwable())
            viewModel.locateCarById(id)
            viewModel.events.value.shouldBeInstanceOf<OnNotFoundCar>()
        }
    }

    @Nested
    inner class `on destroy` {

        @Nested
        inner class `disposable initialized` {

            @Test
            fun `not disposed`() {
                every { viewModel.dispose() } just runs
                every { viewModel getProperty "isDisposableInitialized" } returns true
                every { viewModel getProperty "isDisposableDispose" } returns false
                viewModel.onDestroy()
                verify(exactly = 1) { viewModel.dispose() }
            }

            @Test
            fun disposed() {
                every { viewModel getProperty "isDisposableInitialized" } returns true
                every { viewModel getProperty "isDisposableDispose" } returns true
                viewModel.onDestroy()
                verify(exactly = 0) { viewModel.dispose() }
            }
        }

        @Test
        fun `disposable not initialized`() {
            every { viewModel getProperty "isDisposableInitialized" } returns false
            viewModel.onDestroy()
            verify(exactly = 0) { viewModel.dispose() }
        }
    }
}
