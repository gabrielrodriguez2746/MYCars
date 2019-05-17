package com.mycars.initializers

import com.mycars.base.initializers.Initializer
import com.mycars.carsdata.database.CarsDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DataBaseInitializer @Inject constructor(private val database: CarsDatabase) : Initializer {

    // TODO Improve this for a more robust implementation,
    //  this make sure that every new app launch delete local database
    override fun init() {
        Completable.fromCallable { database.carDatabase().deleteCarsPersitence() }
            .subscribeOn(Schedulers.io())
            .onErrorComplete()
            .subscribe()
    }
}
