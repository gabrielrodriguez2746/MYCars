package com.mycars.activities

import android.os.Bundle
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.mycars.R
import com.mycars.base.listeners.OnFragmentInteraction
import com.mycars.baseui.helpers.setupWithNavController
import com.mycars.carsdetail.fragments.CarDetailFragment
import com.mycars.carshome.fragments.CarListFragment
import com.mycars.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector, OnFragmentInteraction {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    private lateinit var navController: NavController
    private val isNavControllerInitialized: Boolean get() = ::navController.isInitialized

    private lateinit var binding: ActivityMainBinding

    private var toast: Toast? = null

    // TODO This should be delegate
    private val changeBoundsTransition by lazy {
        ChangeBounds().apply {
            duration = ANIMATION_DURATION
            interpolator = AnticipateOvershootInterpolator(1.0f)
        }
    }

    private val detailConstraintSet by lazy {
        ConstraintSet().apply {
            clone(this@MainActivity, R.layout.activity_main_detail_constains)
        }
    }

    private val homeConstraintSet by lazy {
        ConstraintSet().apply {
            clone(this@MainActivity, R.layout.activity_main_home_constrains)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.bnHome.setupWithNavController(
            listOf(R.navigation.main, R.navigation.map),
            supportFragmentManager,
            R.id.flNavController
        ).observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
            this.navController = navController
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (isNavControllerInitialized) {
            val navigateUp = navController.navigateUp()
            if (navigateUp) {
                createActivityTransition(homeConstraintSet)
            }
            navigateUp
        } else {
            false
        }
    }

    override fun onItemClicked(fragment: String, id: String) {
        when (fragment) {
            CarListFragment.CAR_LIST -> {
                createActivityTransition(detailConstraintSet)
                val data = bundleOf(CarDetailFragment.CAR_ID to id.toInt())
                navController.navigate(R.id.action_detail, data)
            }
        }
    }

    override fun onBackPressed() {
        if (isNavControllerInitialized) {
            val navigateUp = navController.navigateUp()
            if (navigateUp) {
                createActivityTransition(homeConstraintSet)
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onNavigateUp(fragment: String) {
        onSupportNavigateUp()
    }

    // TODO Find a better way to show messages
    override fun onMessageToShow(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    override fun androidInjector(): AndroidInjector<Any> = fragmentInjector

    private fun createActivityTransition(constraintSet: ConstraintSet) {
        TransitionManager.beginDelayedTransition(binding.clParent, changeBoundsTransition)
        constraintSet.applyTo(binding.clParent)
    }

    companion object {
        private const val ANIMATION_DURATION = 400L
    }
}
