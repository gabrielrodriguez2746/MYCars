package com.mycars.carslist.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mycars.carslist.R

class CarHorizontalItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val imageCar: ImageView by lazy { findViewById<ImageView>(R.id.ivCar) }
    val textViewCoordinates: TextView by lazy { findViewById<TextView>(R.id.tvCoordinates) }
    val textViewType: TextView by lazy { findViewById<TextView>(R.id.tvType) }
    val textViewHeading: TextView by lazy { findViewById<TextView>(R.id.tvHeading) }

    init {
        inflate(context, R.layout.item_horizontal_car, this)
    }

}