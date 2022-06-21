package com.example.locations.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.locations.R
import com.example.locations.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_place_holder, LocationFragment.newInstance())
                .commit()
        }
    }
}