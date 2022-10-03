package com.example.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.testapp.databinding.FragmentSelectNationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectNationFragment : BottomSheetDialogFragment() {

    private val selectNationViewModel: SelectNationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSelectNationBinding.inflate(layoutInflater, container, false)
        binding.nationRecyclerView.adapter = NationRvAdapter(selectNationViewModel)

        selectNationViewModel.nationItems.observe(viewLifecycleOwner) {
            it?.let { (binding.nationRecyclerView.adapter as NationRvAdapter).setNationList(it) }

        }

        return binding.root
    }
}