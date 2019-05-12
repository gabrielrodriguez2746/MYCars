package com.mycars.base.repository

import io.reactivex.Observable
import io.reactivex.Single

open class BaseRepository<in T, in R, S> {

    open fun getSingleListData(parameters: T?): Single<List<S>> = Single.never()
    open fun getSingleDataByIdentifier(identifier: R): Single<S> = Single.never()
    open fun getObservableData(): Observable<S> = Observable.never()
}