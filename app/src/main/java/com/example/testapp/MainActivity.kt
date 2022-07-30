package com.example.testapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.torang_core.repository.FindRepository
import com.example.torang_core.repository.MapRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mapRepository: MapRepository

    @Inject
    lateinit var findRepository: FindRepository

    @OptIn(InternalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.tv_show)

        findViewById<Button>(R.id.btn).setOnClickListener {
            lifecycleScope.launch {
                findRepository.notifyRequestLocation()
            }
        }

        lifecycleScope.launch {
            findRepository.showRestaurantCardAndFilter().collect(FlowCollector {
                textView.text = if(it) "카드보이기" else "카드가리기"
            })
        }
    }
}