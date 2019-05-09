package com.mycars.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mycars.R
import com.mycars.databinding.ActivityMainBinding
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
}