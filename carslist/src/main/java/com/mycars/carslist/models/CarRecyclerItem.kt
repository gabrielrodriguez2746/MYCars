package com.mycars.carslist.models

import com.mycars.baseui.generics.RecyclerItem

class CarRecyclerItem(private val car: CarWidgetItem) : RecyclerItem<CarWidgetItem, Any>() {
    override fun getId(): Int = car.id
    override fun getComparator(): Int = car.hashCode()
    override fun getContent(): CarWidgetItem = car
}