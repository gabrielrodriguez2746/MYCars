package com.mycars.launcher.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mycars.base.intents.ActivityClassIntent
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var nextActivityClass: ActivityClassIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, nextActivityClass.getClassIntent().java))
        finish()
    }
}
