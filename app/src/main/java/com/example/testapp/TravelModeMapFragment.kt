package com.example.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.screen_map.MapViewModel
import com.example.testapp.databinding.FragmentTravelModeMapBinding
import com.example.torang_core.data.model.Filter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelModeMapFragment : Fragment() {

    /** 뷰모델 */
    private val mViewModel: MapViewModel by activityViewModels()

    private val selectNationViewModel: SelectNationViewModel by activityViewModels()

    private val nationFragment = SelectNationFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTravelModeMapBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.constraintLayout.setOnClickListener {
            nationFragment.show(childFragmentManager, "dialog")
        }

        selectNationViewModel.selected.observe(viewLifecycleOwner) {
            if (nationFragment.isVisible)
                nationFragment.dismiss()
        }

        selectNationViewModel.restaurants.observe(viewLifecycleOwner) {

        }

        binding.viewModel = selectNationViewModel
        return binding.root
    }
}