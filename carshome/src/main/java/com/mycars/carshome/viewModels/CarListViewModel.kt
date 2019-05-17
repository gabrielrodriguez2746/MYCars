package com.mycars.carshome.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.mycars.base.mappers.BaseMapper
import com.mycars.base.repository.BaseRepository
import com.mycars.carsdata.models.cars.Car
import com.mycars.carshome.models.CarRecyclerItem
import com.mycars.carshome.models.CarWidgetItem
import com.mycars.carshome.viewModels.CarListViewModel.CarListViewModelEvents.OnEmptyResults
import com.mycars.carshome.viewModels.CarListViewModel.CarListViewModelEvents.OnItemsUpdated
import com.mycars.carshome.viewModels.CarListViewModel.CarListViewModelEvents.OnRequestError
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CarListViewModel @Inject constructor(
    private val mapper: @JvmSuppressWildcards BaseMapper<Car, CarWidgetItem>,
    private val repository: @JvmSuppressWildcards BaseRepository<Any, Int, Car>
) : ViewModel(), LifecycleObserver {

    private val initialDisposables = CompositeDisposable()
    private val _events = MutableLiveData<CarListViewModelEvents>()

    val events: LiveData<CarListViewModelEvents> get() = _events

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        initialDisposables += repository.getSingleListData(null)
            .subscribeOn(Schedulers.computation())
            .flatMapObservable { Observable.fromIterable(it) }
            .map { CarRecyclerItem(mapper.getFromElement(it)) } // TODO  Mapper should return final item
            .toList()
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
        dispose()
    }

    internal fun dispose() {
        initialDisposables.clear()
    }

    sealed class CarListViewModelEvents {
        object OnEmptyResults : CarListViewModelEvents()
        class OnRequestError(val errorMessage: String?) : CarListViewModelEvents()
        class OnItemsUpdated(val items: List<CarRecyclerItem>) : CarListViewModelEvents()
    }
}
