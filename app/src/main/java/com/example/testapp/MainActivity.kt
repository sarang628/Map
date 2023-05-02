package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.testapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    @Inject
//    lateinit var mapRepository: MapRepository

//    @Inject
//    lateinit var findRepository: FindRepository

    @OptIn(InternalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTestMapFragment.setOnClickListener {
            startActivity(Intent(this, TestMapActivity::class.java))
        }

        binding.btnTestMapUiState.setOnClickListener {

        }

        binding.btnTestMapViewModel.setOnClickListener {

        }

        val textView = findViewById<TextView>(R.id.tv_show)

        findViewById<Button>(R.id.btn).setOnClickListener {
            lifecycleScope.launch {
//                findRepository.notifyRequestLocation()
            }
        }

        lifecycleScope.launch {
            /*findRepository.showRestaurantCardAndFilter().collect(FlowCollector {
                textView.text = if(it) "카드보이기" else "카드가리기"
            })*/
        }
    }
}