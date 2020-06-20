package com.mycars.carsui.delegates

import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
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
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.reflect.KProperty

class MyMarkersMapViewDelegate : MarkersMapViewDelegate {

    private lateinit var mapView: MapView
    private val defaultCameraPadding: Int by lazy {
        mapView.resources.getDimensionPixelSize(R.dimen.carsui_16dp)
    }
    private val markerSize: Int by lazy {
        mapView.resources.getDimensionPixelSize(R.dimen.carsui_marker_size)
    }

    private val data = BehaviorSubject.create<List<MarkerMap>>()

    private lateinit var markerManager: MarkerManager
    private val markerCollection: MarkerManager.Collection by lazy { markerManager.newCollection() }

    private val compositeDisposable = CompositeDisposable()
    private val cameraUpdateSubject = PublishSubject.create<List<LatLng>>()

    private val carImage by lazy {
        val iconFactory = IconGenerator(mapView.context).apply {
            setBackground(null)
            setContentView(ImageView(mapView.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(markerSize, markerSize)
                setImage(R.drawable.ic_car)
            })
        }
        BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon())
    }

    private val taxiImage by lazy {
        val iconFactory = IconGenerator(mapView.context).apply {
            setBackground(null)
            setContentView(ImageView(mapView.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(markerSize, markerSize)
                setImage(R.drawable.ic_taxi)
            })
        }
        BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon())
    }

    private val onMapReadySubject = BehaviorSubject.create<GoogleMap>()

    override fun bind(view: MapView) {
        mapView = view
        view.getMapAsync {
            onMapReadySubject.onNext(it)
            markerManager = MarkerManager(it)
        }
    }

    override fun init(data: List<MarkerMap>) {
        this.data.onNext(data)
    }

    override fun getValue(thisRef: MarkersMapView, property: KProperty<*>): MarkersMapViewDelegate {
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mapView.onResume()
        subscribeToCameraUpdates()
        subscribeToLocationChanges()
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
        compositeDisposable += Observable.combineLatest(data
            .distinctUntilChanged(),
            onMapReadySubject,
            BiFunction<List<MarkerMap>, GoogleMap, List<MarkerMap>> { locations, _ ->
                locations
            })
            .doOnNext { requestCameraUpdate(mapToLocation(it)) }
            .flatMap { Observable.fromIterable(it) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = ::addMarkers)
    }

    private fun subscribeToCameraUpdates() {
        compositeDisposable += Observable.combineLatest(cameraUpdateSubject
            .subscribeOn(Schedulers.computation())
            .debounce(CAMERA_CHANGES_DEBOUNCE, TimeUnit.MILLISECONDS),
            onMapReadySubject.subscribeOn(Schedulers.computation()),
            BiFunction<List<LatLng>, GoogleMap, Pair<List<LatLng>, GoogleMap>> { locations, map ->
                locations to map
            })
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                val (locations, map) = it
                map.updateCamera(locations)
            })
    }

    private fun GoogleMap.updateCamera(locations: List<LatLng>) {
        getLatLngBounds(locations)?.let { bounds ->
            val cameraUpdate = if (locations.size > 1) {
                CameraUpdateFactory.newLatLngBounds(bounds, defaultCameraPadding)
            } else {
                CameraUpdateFactory.newLatLngZoom(bounds.center, MAX_ZOOM_PREFERENCE)
            }
            animateCamera(cameraUpdate, CAMERA_ANIMATION_DURATION, null)
        }
    }

    private fun addMarkers(markerMap: MarkerMap) {
        markerCollection.addMarker(
            MarkerOptions()
                .icon(getImageFromType(markerMap.type))
                .position(with(markerMap) { LatLng(latitude, longitude) })
                .anchor(MARKER_ANCHOR, MARKER_ANCHOR)
        )
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

    private fun getImageFromType(type: String): BitmapDescriptor {
        return if (type == "POOLING") {
            carImage
        } else {
            taxiImage
        }
    }

    private fun disposeSubscription() {
        compositeDisposable.clear()
    }

    companion object {
        private const val MAX_ZOOM_PREFERENCE = 15.5f
        private const val MARKER_ANCHOR = 0.5f
        private const val CAMERA_ANIMATION_DURATION = 500
        private const val CAMERA_CHANGES_DEBOUNCE = 500L
    }
}
