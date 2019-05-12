package com.mycars.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.mycars.R
import com.mycars.base.listeners.OnFragmentInteraction
import com.mycars.baseui.helpers.setupWithNavController
import com.mycars.carsdetail.fragments.CarDetailFragment
import com.mycars.carslist.fragments.CarListFragment
import com.mycars.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, OnFragmentInteraction {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var navController: NavController
    private val isNavControllerInitialized: Boolean get() = ::navController.isInitialized

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.bnHome.setupWithNavController(
            listOf(R.navigation.main, R.navigation.main),
            supportFragmentManager,
            R.id.flNavController
        ).observe(this, Observer { navController ->
            this.navController = navController
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (isNavControllerInitialized) {
            navController.navigateUp()
        } else {
            false
        }
    }

    override fun onItemClicked(fragment: String, id: String) {
        when (fragment) {
            CarListFragment.CAR_LIST -> {
                val data = bundleOf(CarDetailFragment.CAR_ID to id.toInt())
                navController.navigate(R.id.action_detail, data)
            }
        }
    }

    override fun onBackPressed() {
        if (isNavControllerInitialized && !navController.navigateUp()) {
            super.onBackPressed()
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

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}