package com.mycars.carsui.delegates

import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.maps.MapView
import com.mycars.carsui.models.MarkerMap
import com.mycars.carsui.widgets.MarkersMapView
import io.reactivex.Observable
import kotlin.properties.ReadOnlyProperty

interface MarkersMapViewDelegate : ReadOnlyProperty<MarkersMapView, MarkersMapViewDelegate>, LifecycleObserver {

    fun init(data: List<MarkerMap>)

    fun bind(view: MapView)

}