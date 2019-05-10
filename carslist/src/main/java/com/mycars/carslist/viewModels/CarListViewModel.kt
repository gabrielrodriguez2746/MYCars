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
import com.mycars.data.models.cars.Car
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class CarListViewModel @Inject constructor(
    private val mapper: @JvmSuppressWildcards BaseMapper<Car, CarWidgetItem>,
    private val repository: @JvmSuppressWildcards BaseRepository<Any, Car>
) : ViewModel(), LifecycleObserver {

    internal val initialDisposables = CompositeDisposable()
    private val _events = MutableLiveData<CarListViewModelEvents>()

    val events: LiveData<CarListViewModelEvents> get() = _events

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        initialDisposables += repository.getSingleListData(null)
            .map { it.map { car -> CarRecyclerItem(mapper.getFromElement(car)) } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy  (onError = {
                _events.postValue(CarListViewModelEvents.OnServerError)
            }, onSuccess = {
                if (it.isEmpty()) {
                    _events.postValue(CarListViewModelEvents.OnEmptyResults)
                } else {
                    _events.postValue(CarListViewModelEvents.OnItemsUpdated(it))
                }

            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        initialDisposables.clear()
    }

    sealed class CarListViewModelEvents {
        object OnServerError : CarListViewModelEvents() // TODO From a couple PR errors will have structure
        object OnEmptyResults : CarListViewModelEvents()
        class OnItemsUpdated(val items: List<CarRecyclerItem>) : CarListViewModelEvents()
    }

}