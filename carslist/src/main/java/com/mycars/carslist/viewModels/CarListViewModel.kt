package com.mycars.carslist.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.mycars.base.mappers.BaseMapper
import com.mycars.base.repository.BaseRepository
import com.mycars.carslist.models.CarRecyclerItem
import com.mycars.carslist.models.CarWidgetItem
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents.OnEmptyResults
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents.OnItemsUpdated
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents.OnRequestError
import com.mycars.carsui.models.MarkerMap
import com.mycars.data.models.cars.Car
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CarListViewModel @Inject constructor(
    private val mapper: @JvmSuppressWildcards BaseMapper<Car, CarWidgetItem>,
    private val repository: @JvmSuppressWildcards BaseRepository<Any, Car>
) : ViewModel(), LifecycleObserver {

    internal val initialDisposables = CompositeDisposable()
    private val _events = MutableLiveData<CarListViewModelEvents>()
    private val locationsSubject = BehaviorSubject.create<List<MarkerMap>>()

    val locations : Observable<List<MarkerMap>> get() = locationsSubject.hide()
    val events: LiveData<CarListViewModelEvents> get() = _events

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        initialDisposables += repository.getSingleListData(null)
            .subscribeOn(Schedulers.computation())
            .doOnSuccess { if (it.isNotEmpty()) locationsSubject.onNext(mapMarkerMap(it)) }
            .map(::mapToListItem)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                _events.postValue(OnRequestError(it.message))
            }, onSuccess = {
                if (it.isEmpty()) {
                    _events.postValue(OnEmptyResults)
                } else {
                    _events.postValue(OnItemsUpdated(it))
                }

            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        initialDisposables.clear()
    }

    internal fun mapMarkerMap(cars: List<Car>): List<MarkerMap> {
        return cars.map { car -> with(car.coordinate) { MarkerMap(latitude, longitude, car.type)  } }
    }

    internal fun mapToListItem(cars: List<Car>): List<CarRecyclerItem> {
        return cars.map { car -> CarRecyclerItem(mapper.getFromElement(car)) }
    }

    sealed class CarListViewModelEvents {
        object OnEmptyResults : CarListViewModelEvents()
        class OnRequestError(val errorMessage: String?) : CarListViewModelEvents()
        class OnItemsUpdated(val items: List<CarRecyclerItem>) : CarListViewModelEvents()
    }

}