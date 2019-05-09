package com.mycars.carslist.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.mycars.base.repository.BaseRepository
import com.mycars.carslist.R
import com.mycars.carslist.models.CarRecyclerItem
import com.mycars.carslist.models.CarWidgetItem
import com.mycars.data.models.cars.Car
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class CarListViewModel @Inject constructor(private val repository: @JvmSuppressWildcards BaseRepository<Any, Car>) :
    ViewModel(),
    LifecycleObserver {

    private val initialDisposables = CompositeDisposable()
    private val _events = MutableLiveData<CarListViewModelEvents>()

    val events: LiveData<CarListViewModelEvents> get() = _events

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        initialDisposables += repository.getSingleListData(null)
            .map {
                it.map { car ->
                    // TODO Delegate the real thing to a mapper and test it, add the other icon, adjust icon colors
                    CarRecyclerItem(CarWidgetItem(car.id, R.drawable.ic_taxi, car.type, "${car.coordinate}", "${car.heading}"))
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                _events.postValue(
                    CarListViewModelEvents.OnItemsUpdated(it)
                )
            }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        initialDisposables.clear()
    }

    sealed class CarListViewModelEvents {
        class OnItemsUpdated(val items: List<CarRecyclerItem>) : CarListViewModelEvents()
    }

}