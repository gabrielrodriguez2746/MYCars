package com.mycars.carsdetail.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mycars.base.listeners.OnFragmentInteraction
import com.mycars.carsdetail.R
import com.mycars.carsdetail.databinding.FragmentCarDetailBinding
import com.mycars.carsdetail.viewModels.CarDetailViewModel
import com.mycars.carsdetail.viewModels.CarDetailViewModel.CarDetailViewModelEvents
import com.mycars.carsdetail.viewModels.CarDetailViewModel.CarDetailViewModelEvents.OnAdjustTitle
import com.mycars.carsdetail.viewModels.CarDetailViewModel.CarDetailViewModelEvents.OnNotFoundCar
import com.mycars.carsdetail.viewModels.CarDetailViewModel.CarDetailViewModelEvents.OnMapItems
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CarDetailFragment : Fragment() {

    private lateinit var binding: FragmentCarDetailBinding
    private lateinit var viewModel: CarDetailViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val getActivityListener : OnFragmentInteraction? get() = activity as? OnFragmentInteraction
    private val supportActionBar : ActionBar? get() = (activity as AppCompatActivity).supportActionBar

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_detail, container, false)
        return with(binding) {
            mvCars.onCreate(savedInstanceState)
            root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val carId = arguments?.getInt(CAR_ID, -1) ?: -1
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CarDetailViewModel::class.java)
        viewModel.events.observe(viewLifecycleOwner, Observer(::processEvents))
        viewModel.locateCarById(carId)

        activity?.lifecycle?.apply {
            addObserver(viewModel)
            addObserver(binding.mvCars)
        }

    }

    private fun processEvents(event: CarDetailViewModelEvents) {
        when (event) {
            is OnNotFoundCar -> notifyCloseError()
            is OnMapItems -> binding.mvCars.init(event.items)
            is OnAdjustTitle -> supportActionBar?.title = event.title
        }
    }

    private fun notifyCloseError() {
        getActivityListener?.onMessageToShow("Error") // TODO Define generic error
        getActivityListener?.onNavigateUp(CAR_DETAIL)
    }

    companion object {
        const val CAR_ID = "car_id"
        const val CAR_DETAIL = "CAR_DETAIL"
    }
}