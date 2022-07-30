package com.example.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.testapp.databinding.FragmentSelectNationBinding
import com.example.torang_core.util.Logger
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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