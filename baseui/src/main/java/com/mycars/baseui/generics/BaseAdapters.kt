package com.mycars.baseui.generics

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T : RecyclerItem<*, *>> :
    ListAdapter<T, ViewHolder<*, *>>(RecyclerItemDiffCallback<T>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*, *> {
        return getViewHolder(parent, viewType)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder<*, *>, position: Int) {
        getItem(position)?.getContent()?.let {
            (holder as ViewHolder<Any, Any>).bind(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder<*, *>, position: Int, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            (holder as ViewHolder<Any, Any>).updateBind(it)
        } ?: super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.getType() ?: -1
    }

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*, *>
}

abstract class RecyclerItem<out R, out S> {
    abstract fun getId(): Int
    abstract fun getComparator(): Any?
    abstract fun getContent(): R
    open fun getType(): Int = 1
    open fun getDiffResolver(): S? = null
}

class RecyclerItemDiffCallback<T : RecyclerItem<*, *>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(firsteItem: T, secondItem: T): Boolean = firsteItem.getId() == secondItem.getId()

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(firsteItem: T, secondItem: T): Boolean {
        return firsteItem.getComparator() == secondItem.getComparator()
    }

    override fun getChangePayload(oldItem: T, newItem: T): Any? = newItem.getDiffResolver()
}

abstract class ViewHolder<T : Any, R : Any>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)

    open fun updateBind(item: R) = Unit
}