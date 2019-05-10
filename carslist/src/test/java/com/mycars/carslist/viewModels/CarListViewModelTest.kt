package com.mycars.carslist.viewModels

import com.mycars.base.mappers.BaseMapper
import com.mycars.base.repository.BaseRepository
import com.mycars.basetest.InstantExecutorExtension
import com.mycars.carslist.models.CarWidgetItem
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents.OnItemsUpdated
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents.OnEmptyResults
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents.OnRequestError
import com.mycars.data.models.cars.Car
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
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
class CarListViewModelTest {

    private val mapper: BaseMapper<Car, CarWidgetItem> = mockk()
    private val repository: BaseRepository<Any, Car> = mockk()

    lateinit var viewModel: CarListViewModel

    @BeforeAll
    fun beforeAll() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @AfterAll
    fun afterAll() {
        RxAndroidPlugins.reset()
    }

    @BeforeEach
    fun beforeEach() {
        viewModel = spyk(CarListViewModel(mapper, repository))
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

                every { mapper.getFromElement(any()) } returns mockk()
                every { repository.getSingleListData(any()) } returns Single.just(listOf(mockk()))

                viewModel.onCreate()
                viewModel.events.value.shouldBeInstanceOf<OnItemsUpdated>()
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
        viewModel.onDestroy()
        verify(exactly = 0) { viewModel.initialDisposables.clear() }
    }


}