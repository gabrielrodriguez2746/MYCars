package com.mycars.network.repository

import com.mycars.base.repository.BaseRepository
import com.mycars.network.rest.CarsService
import com.mycars.data.models.cars.Car
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CarRepository @Inject constructor(private val service: CarsService) : BaseRepository<Any, Car>() {

    // TODO Save on data base and verify if data base contains values
    override fun getSingleListData(parameters: Any?): Single<List<Car>> {
        return service.getCarWrapper()
            .subscribeOn(Schedulers.io())
            .map { it.cars }
            .doOnSuccess {
                // TODO Save on data base
            }
    }

}