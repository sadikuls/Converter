package com.sadikul.currencyexchange.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sadikul.currencyexchange.R
import com.sadikul.currencyexchange.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}