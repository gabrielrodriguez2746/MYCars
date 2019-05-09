package com.mycars.base.repository

import io.reactivex.Observable
import io.reactivex.Single

open class BaseRepository<in T, R> {

    open fun getSingleListData(parameters: T?): Single<List<R>> = Single.just(listOf())
    open fun getObservableData(): Observable<R> = Observable.fromIterable(listOf())
}