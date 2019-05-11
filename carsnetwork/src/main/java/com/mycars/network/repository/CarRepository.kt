package com.mycars.network.repository

import com.mycars.base.repository.BaseRepository
import com.mycars.carsdata.dao.CarsDao
import com.mycars.carsdata.models.cars.Car
import com.mycars.network.rest.CarsService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val service: CarsService,
    private val dao: CarsDao
) : BaseRepository<Any, Car>() {

    override fun getSingleListData(parameters: Any?): Single<List<Car>> {
        return dao.getCarsPersistenceList()
            .toSingle()
            .flatMap(::validateEmptyPersistence)
            .onErrorResumeNext { getDataFromServer() }
    }

    internal fun validateEmptyPersistence(cars: List<Car>): Single<List<Car>> {
        return if (cars.isEmpty()) {
            getDataFromServer()
        } else {
            Single.just(cars)
        }
    }

    internal fun getDataFromServer(): Single<List<Car>> {
        return service.getCarWrapper()
            .subscribeOn(Schedulers.io())
            .map { it.cars }
            .observeOn(Schedulers.io())
            .doOnSuccess { dao.insert(it) }
    }

}