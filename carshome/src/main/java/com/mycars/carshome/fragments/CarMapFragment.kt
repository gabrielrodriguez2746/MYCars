package com.mycars.carshome.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mycars.base.listeners.OnFragmentInteraction
import com.mycars.carshome.R
import com.mycars.carshome.databinding.FragmentCarMapBinding
import com.mycars.carshome.viewModels.CarMapsViewModel
import com.mycars.carshome.viewModels.CarMapsViewModel.CarMapsViewModelEvents
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CarMapFragment : Fragment() {

    private lateinit var binding: FragmentCarMapBinding
    private val isBindingInitialized: Boolean get() = ::binding.isInitialized

    private lateinit var viewModel: CarMapsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val getActivityListener: OnFragmentInteraction? get() = activity as? OnFragmentInteraction

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!isBindingInitialized) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_map, container, false)
            binding.mvCars.onCreate(savedInstanceState)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CarMapsViewModel::class.java)
        viewModel.events.observe(viewLifecycleOwner, Observer(::processEvents))

        activity?.lifecycle?.apply {
            addObserver(viewModel)
            addObserver(binding.mvCars)
        }

    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mvCars.onLowMemory()
    }

    private fun processEvents(event: CarMapsViewModelEvents) {
        when (event) {
            is CarMapsViewModelEvents.OnMapItems -> binding.mvCars.init(event.items)
            is CarMapsViewModelEvents.OnEmptyResults -> showEmptyData()
            is CarMapsViewModelEvents.OnRequestError -> processError(event.errorMessage.orEmpty())
        }
    }

    private fun processError(message: String) {
        getActivityListener?.onMessageToShow(message)
    }

    // TODO Show data 0 view
    private fun showEmptyData() = Unit

    companion object {
        const val CAR_MAP = "CAR_MAP"
    }
}