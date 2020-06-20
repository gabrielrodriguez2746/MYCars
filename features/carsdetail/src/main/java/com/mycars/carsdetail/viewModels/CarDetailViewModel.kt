package com.mycars.carsdetail.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.mycars.base.providers.ResourceProvider
import com.mycars.base.repository.BaseRepository
import com.mycars.carsdata.models.cars.Car
import com.mycars.carsdata.models.cars.Coordinate
import com.mycars.carsdetail.R
import com.mycars.carsdetail.viewModels.CarDetailViewModel.CarDetailViewModelEvents.OnAdjustTitle
import com.mycars.carsdetail.viewModels.CarDetailViewModel.CarDetailViewModelEvents.OnMapItems
import com.mycars.carsdetail.viewModels.CarDetailViewModel.CarDetailViewModelEvents.OnNotFoundCar
import com.mycars.carsui.models.MarkerMap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CarDetailViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val repository: @JvmSuppressWildcards BaseRepository<Any, Int, Car>
) : ViewModel(), LifecycleObserver {

    private lateinit var disposable: Disposable
    private val isDisposableInitialized: Boolean get() = ::disposable.isInitialized
    private val isDisposableDispose: Boolean get() = disposable.isDisposed

    private val _events = MutableLiveData<CarDetailViewModelEvents>()

    val events: LiveData<CarDetailViewModelEvents> get() = _events

    fun locateCarById(id: Int) {
        disposable = repository.getSingleDataByIdentifier(id)
            .doOnSuccess {
                with(it.coordinate) { _events.postValue(OnAdjustTitle(getFormattedCoordinates())) }
            }
            .map { with(it.coordinate) { MarkerMap(latitude, longitude, it.type) } }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                _events.postValue(OnNotFoundCar)
            }, onSuccess = {
                _events.postValue(OnMapItems(listOf(it)))
            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (isDisposableInitialized && !isDisposableDispose) {
            dispose()
        }
    }

    internal fun Coordinate.getFormattedCoordinates(): String {
        return resourceProvider.getString(
            R.string.carsdetail_coordinates,
            latitude,
            longitude
        )
    }

    internal fun dispose() {
        disposable.dispose()
    }

    sealed class CarDetailViewModelEvents {
        object OnNotFoundCar : CarDetailViewModelEvents()
        class OnMapItems(val items: List<MarkerMap>) : CarDetailViewModelEvents()
        class OnAdjustTitle(val title: String) : CarDetailViewModelEvents()
    }
}
