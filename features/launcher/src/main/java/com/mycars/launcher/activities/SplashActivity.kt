package com.mycars.launcher.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mycars.base.navigation.NavigationProvider
import com.mycars.base.navigation.NavigationType
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        navigationProvider.navigate(this, NavigationType.HOME)
        finish()
    }
}
