package com.mycars.carshome.fragments

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.mycars.base.listeners.OnFragmentInteraction
import com.mycars.baseui.decorators.MediaSpaceDecoration
import com.mycars.baseui.generics.BaseListAdapter
import com.mycars.baseui.generics.ViewHolder
import com.mycars.baseui.helpers.addRippleForeground
import com.mycars.baseui.models.RecyclerViewConfiguration
import com.mycars.carshome.R
import com.mycars.carshome.databinding.FragmentCarListBinding
import com.mycars.carshome.models.CarRecyclerItem
import com.mycars.carshome.models.CarWidgetItem
import com.mycars.carshome.viewModels.CarListViewModel
import com.mycars.carshome.viewModels.CarListViewModel.CarListViewModelEvents
import com.mycars.carshome.viewModels.CarListViewModel.CarListViewModelEvents.OnEmptyResults
import com.mycars.carshome.viewModels.CarListViewModel.CarListViewModelEvents.OnItemsUpdated
import com.mycars.carshome.viewModels.CarListViewModel.CarListViewModelEvents.OnRequestError
import com.mycars.carsui.databinding.ItemHorizontalCarBinding
import com.mycars.carsui.databinding.ItemVerticalCarBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CarListFragment : Fragment() {

    private lateinit var binding: FragmentCarListBinding
    private val isBindingInitialized: Boolean get() = ::binding.isInitialized

    private lateinit var viewModel: CarListViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val itemsAdapter by lazy { CarListAdapter() }
    private val displayMetrics by lazy {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        metrics
    }
    private val itemVerticalHeight by lazy {
        (displayMetrics.heightPixels * VERTICAL_WIDGET_HEIGHT_PERCENTAGE).toInt()
    }
    private val itemHorizontalHeight by lazy {
        (displayMetrics.heightPixels * HORIZONTAL_WIDGET_HEIGHT_PERCENTAGE).toInt()
    }

    private val getActivityListener: OnFragmentInteraction? get() = activity as? OnFragmentInteraction

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!isBindingInitialized) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_list, container, false)
            with(binding) {
                recyclerConfiguration = RecyclerViewConfiguration(
                    layoutManager = GridLayoutManager(context, 2),
                    isNestedScroll = true,
                    adapter = itemsAdapter,
                    decorator = MediaSpaceDecoration(resources.getDimensionPixelSize(R.dimen.space_small))
                )
            }
        }
        return binding.root
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

    private fun processError(message: String) {
        getActivityListener?.onMessageToShow(message)
        if (itemsAdapter.itemCount == 0) {
            showEmptyData()
        }
    }

    // TODO Show data 0 view
    private fun showEmptyData() = Unit

    // TODO All this manage it with delegates
    inner class CarListAdapter : BaseListAdapter<CarRecyclerItem>() {
        override fun getViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*, *> {
            return if (viewType == 0) {
                val view = DataBindingUtil.inflate<ItemVerticalCarBinding>(
                    layoutInflater,
                    R.layout.item_vertical_car,
                    parent,
                    false
                )
                view.root.apply {
                    isClickable = true
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, itemVerticalHeight)
                    addRippleForeground()
                }
                CarListVerticalViewHolder(view)
            } else {
                val view = DataBindingUtil.inflate<ItemHorizontalCarBinding>(
                    layoutInflater,
                    R.layout.item_horizontal_car,
                    parent,
                    false
                )
                view.root.apply {
                    isClickable = true
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, itemHorizontalHeight)
                    addRippleForeground()
                }
                CarListHorizontalViewHolder(view)
            }
        }
    }

    inner class CarListVerticalViewHolder(private val binding: ItemVerticalCarBinding) :
        ViewHolder<CarWidgetItem, Any>(binding.root) {

        override fun bind(item: CarWidgetItem) {
            with(binding) {
                image = item.imageType
                coordinates = item.coordinates
                type = item.type
                heading = item.heading
                root.setOnClickListener { getActivityListener?.onItemClicked(CAR_LIST, item.id.toString()) }
            }
        }
    }

    inner class CarListHorizontalViewHolder(private val binding: ItemHorizontalCarBinding) :
        ViewHolder<CarWidgetItem, Any>(binding.root) {

        override fun bind(item: CarWidgetItem) {
            with(binding) {
                image = item.imageType
                coordinates = item.coordinates
                type = item.type
                heading = item.heading
                root.setOnClickListener { getActivityListener?.onItemClicked(CAR_LIST, item.id.toString()) }
            }
        }
    }

    companion object {
        const val CAR_LIST = "CAR_LIST"

        private const val VERTICAL_WIDGET_HEIGHT_PERCENTAGE = 0.33
        private const val HORIZONTAL_WIDGET_HEIGHT_PERCENTAGE = 0.18
    }
}
