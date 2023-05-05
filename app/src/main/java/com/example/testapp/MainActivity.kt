package com.example.testapp

import android.app.Activity
import android.os.Bundle
import com.example.testapp.databinding.ActivityMainBinding

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}