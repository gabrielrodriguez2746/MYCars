package com.mycars.carshome.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.mycars.base.repository.BaseRepository
import com.mycars.carsdata.models.cars.Car
import com.mycars.carshome.viewModels.CarMapsViewModel.CarMapsViewModelEvents.OnEmptyResults
import com.mycars.carshome.viewModels.CarMapsViewModel.CarMapsViewModelEvents.OnMapItems
import com.mycars.carshome.viewModels.CarMapsViewModel.CarMapsViewModelEvents.OnRequestError
import com.mycars.carsui.models.MarkerMap
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CarMapsViewModel @Inject constructor(
    private val repository: @JvmSuppressWildcards BaseRepository<Any, Int, Car>
) : ViewModel(), LifecycleObserver {

    private val initialDisposables = CompositeDisposable()
    private val _events = MutableLiveData<CarMapsViewModelEvents>()

    val events: LiveData<CarMapsViewModelEvents> get() = _events

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        initialDisposables += repository.getSingleListData(null)
            .subscribeOn(Schedulers.computation())
            .flatMapObservable { Observable.fromIterable(it) }
            .map { with(it.coordinate) { MarkerMap(latitude, longitude, it.type) } }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                _events.postValue(OnRequestError(it.message))
            }, onSuccess = {
                if (it.isEmpty()) {
                    _events.postValue(OnEmptyResults)
                } else {
                    _events.postValue(OnMapItems(it))
                }
            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        dispose()
    }

    internal fun dispose() {
        initialDisposables.clear()
    }

    sealed class CarMapsViewModelEvents {
        object OnEmptyResults : CarMapsViewModelEvents()
        class OnRequestError(val errorMessage: String?) : CarMapsViewModelEvents()
        class OnMapItems(val items: List<MarkerMap>) : CarMapsViewModelEvents()
    }
}
