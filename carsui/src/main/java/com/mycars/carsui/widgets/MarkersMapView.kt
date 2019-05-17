package com.mycars.carsui.widgets

import android.content.Context
import android.util.AttributeSet
import com.google.android.gms.maps.MapView
import com.mycars.carsui.delegates.MarkersMapViewDelegate
import com.mycars.carsui.delegates.MyMarkersMapViewDelegate

class MarkersMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr), MarkersMapViewDelegate by MyMarkersMapViewDelegate() {

    init {
        bind(this)
    }
}
