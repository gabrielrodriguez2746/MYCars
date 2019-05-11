package com.mycars.carslist.fragments

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.mycars.baseui.decorators.MediaSpaceDecoration
import com.mycars.baseui.generics.BaseListAdapter
import com.mycars.baseui.generics.ViewHolder
import com.mycars.baseui.generics.addRippleForeground
import com.mycars.baseui.models.RecyclerViewConfiguration
import com.mycars.carslist.R
import com.mycars.carslist.databinding.FragmentCarListBinding
import com.mycars.carslist.models.CarRecyclerItem
import com.mycars.carslist.models.CarWidgetItem
import com.mycars.carslist.viewModels.CarListViewModel
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents.OnEmptyResults
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents.OnItemsUpdated
import com.mycars.carslist.viewModels.CarListViewModel.CarListViewModelEvents.OnRequestError
import com.mycars.carslist.widget.CarHorizontalItem
import com.mycars.carslist.widget.CarVerticalItem
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CarListFragment : Fragment() {

    private lateinit var viewModel: CarListViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val itemsAdapter by lazy { CarListAdapter() }
    private val displayMetrics by lazy {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        metrics
    }
    private val itemVerticalHeight by lazy { (displayMetrics.heightPixels * 0.33).toInt() }
    private val itemHorizontalHeight by lazy { (displayMetrics.heightPixels * 0.25).toInt() }

    private var toast: Toast? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return with(
            DataBindingUtil.inflate<FragmentCarListBinding>(inflater, R.layout.fragment_car_list, container, false)
        ) {
            recyclerConfiguration = RecyclerViewConfiguration(
                layoutManager = GridLayoutManager(context, 2),
                isNestedScroll = true,
                adapter = itemsAdapter,
                decorator = MediaSpaceDecoration(resources.getDimensionPixelSize(R.dimen.space_small))
            )
            root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CarListViewModel::class.java)
        viewModel.events.observe(viewLifecycleOwner, Observer(::processEvents))
        activity?.lifecycle?.addObserver(viewModel)
    }

    private fun processEvents(event: CarListViewModelEvents) {
        when (event) {
            is OnItemsUpdated -> itemsAdapter.submitList(event.items)
            is OnEmptyResults -> showEmptyData()
            is OnRequestError -> processError(event.errorMessage.orEmpty())
        }
    }

    // TODO Find a better way to show errors
    private fun processError(message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast?.show()
        if (itemsAdapter.itemCount == 0) {
            showEmptyData()
        }
    }

    // TODO Show data 0 view
    private fun showEmptyData() = Unit

    // TODO All this manage it with delegates
    inner class CarListAdapter : BaseListAdapter<CarRecyclerItem>() {
        override fun getViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*, *> {
            return if (viewType == 1) {
                CarListVerticalViewHolder(CarVerticalItem(parent.context).apply {
                    isClickable = true
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemVerticalHeight)
                    addRippleForeground()
                })
            } else {
                CarListHorizontalViewHolder(CarHorizontalItem(parent.context).apply {
                    isClickable = true
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHorizontalHeight)
                    addRippleForeground()
                })
            }
        }
    }

    inner class CarListVerticalViewHolder(private val view: CarVerticalItem) : ViewHolder<CarWidgetItem, Any>(view) {

        override fun bind(item: CarWidgetItem) {
            with(view) {
                textViewCoordinates.text = item.coordinates
                textViewType.text = item.type
                textViewHeading.text = item.heading
                imageCar.setImageResource(item.imageType)
                setOnClickListener { Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show() }
            }
        }
    }

    inner class CarListHorizontalViewHolder(private val view: CarHorizontalItem) : ViewHolder<CarWidgetItem, Any>(view) {

        override fun bind(item: CarWidgetItem) {
            with(view) {
                textViewCoordinates.text = item.coordinates
                textViewType.text = item.type
                textViewHeading.text = item.heading
                imageCar.setImageResource(item.imageType)
                setOnClickListener { Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show() }
            }
        }
    }
}