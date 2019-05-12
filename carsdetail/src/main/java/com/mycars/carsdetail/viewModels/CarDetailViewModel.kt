package com.mycars.carsdetail.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.mycars.base.repository.BaseRepository
import com.mycars.carsui.models.MarkerMap
import com.mycars.carsdata.models.cars.Car
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CarDetailViewModel @Inject constructor(
    private val repository: @JvmSuppressWildcards BaseRepository<Any, Int, Car>
) : ViewModel(), LifecycleObserver {

    private lateinit var disposable : Disposable
    private val isDisposableInitialized: Boolean get() = ::disposable.isInitialized

    private val _events = MutableLiveData<CarDetailViewModelEvents>()
    private val locationsSubject = BehaviorSubject.create<List<MarkerMap>>()

    val locations : Observable<List<MarkerMap>> get() = locationsSubject.hide()
    val events: LiveData<CarDetailViewModelEvents> get() = _events

    fun locateCarById(id: Int) {
        disposable = repository.getSingleDataByIdentifier(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                _events.postValue(CarDetailViewModelEvents.OnNotFoundCar)
            }, onSuccess = {
                locationsSubject.onNext(mapMarkerMap(listOf(it)))
            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (isDisposableInitialized && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    internal fun mapMarkerMap(cars: List<Car>): List<MarkerMap> {
        return cars.map { car -> with(car.coordinate) { MarkerMap(latitude, longitude, car.type)  } }
    }

    sealed class CarDetailViewModelEvents {
        object OnNotFoundCar : CarDetailViewModelEvents()
    }

}