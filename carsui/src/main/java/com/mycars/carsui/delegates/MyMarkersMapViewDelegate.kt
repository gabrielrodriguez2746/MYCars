package com.mycars.carsui.delegates

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.MarkerManager
import com.google.maps.android.ui.IconGenerator
import com.mycars.carsui.R
import com.mycars.carsui.binding.setImage
import com.mycars.carsui.models.MarkerMap
import com.mycars.carsui.widgets.MarkersMapView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.reflect.KProperty

class MyMarkersMapViewDelegate : MarkersMapViewDelegate {

    private lateinit var mapView: MapView
    private val defaultCameraPadding : Int by lazy {
        mapView.resources.getDimensionPixelSize(R.dimen.carsui_16dp)
    }
    private val markerSize : Int by lazy {
        mapView.resources.getDimensionPixelSize(R.dimen.carsui_marker_size)
    }

    private lateinit var data: Observable<List<MarkerMap>>
    private val isDataSourceInitialized: Boolean get() = ::data.isInitialized

    private lateinit var map: GoogleMap
    private val isMapInitialized: Boolean get() = ::map.isInitialized

    private lateinit var markerManager: MarkerManager
    private val markerCollection: MarkerManager.Collection by lazy { markerManager.newCollection() }

    private val compositeDisposable = CompositeDisposable()
    private val cameraUpdateSubject = PublishSubject.create<List<LatLng>>()

    override fun bind(view: MapView) {
        mapView = view
        view.getMapAsync {
            map = it
            markerManager = MarkerManager(it)
            onResume()
        }
    }

    override fun init(data: Observable<List<MarkerMap>>) {
        this.data = data
    }

    override fun getValue(thisRef: MarkersMapView, property: KProperty<*>): MarkersMapViewDelegate {
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mapView.onResume()
        if (isDataSourceInitialized) {
            disposeSubscription()
            subscribeToLocationChanges()
            subscribeToCameraUpdates()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mapView.onPause()
        disposeSubscription()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        mapView.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mapView.onDestroy()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        mapView.onStart()
    }

    private fun subscribeToLocationChanges() {
        compositeDisposable += data
            .distinctUntilChanged()
            .doOnNext { requestCameraUpdate(mapToLocation(it)) }
            .flatMap { Observable.fromIterable(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = ::addMarkers)
    }

    private fun subscribeToCameraUpdates() {
        compositeDisposable += cameraUpdateSubject
            .debounce(500L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = ::updateCamera)
    }

    private fun updateCamera(locations: List<LatLng>) {
        getLatLngBounds(locations)?.let { bounds ->

            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, defaultCameraPadding)
            map.animateCamera(cameraUpdate, 500, null)

        }
    }

    private fun addMarkers(markerMap: MarkerMap) {
        if (isMapInitialized) {
            val iconFactory = IconGenerator(mapView.context).apply {
                setBackground(null)
                setContentView(ImageView(mapView.context).apply {
                    layoutParams = ViewGroup.MarginLayoutParams(markerSize, markerSize)
                    setImage(getImageFromType(markerMap.type))
                })
            }

            markerCollection.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon()))
                    .position(with(markerMap) { LatLng(latitude, longitude) } )
                    .anchor(0.5f, 0.5f)
            )
        }
    }

    private fun requestCameraUpdate(locations: List<LatLng>) {
        cameraUpdateSubject.onNext(locations)
    }

    private fun mapToLocation(data: List<MarkerMap>): List<LatLng> {
        return data.map {
            val (latitude, longitude) = it
            LatLng(latitude, longitude)
        }
    }

    private fun getLatLngBounds(locations: List<LatLng>): LatLngBounds? {

        return if (locations.isNotEmpty()) {
            val builder = LatLngBounds.Builder()
            locations.forEach { builder.include(it) }
            builder.build()
        } else {
            null
        }
    }

    @DrawableRes
    private fun getImageFromType(type: String): Int {
        return if (type == "POOLING") {
            R.drawable.ic_car
        } else {
            R.drawable.ic_taxi
        }
    }

    private fun disposeSubscription() {
        compositeDisposable.clear()
    }

}