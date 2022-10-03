package com.example.testapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.databinding.ItemNationBinding
import com.sryang.torang_core.data.NationItem

class NationRvAdapter(val selectNationViewModel: SelectNationViewModel) :
    RecyclerView.Adapter<NationRvHolder>() {
    private var nationItems = ArrayList<NationItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NationRvHolder {
        return NationRvHolder(
            ItemNationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            binding.viewModel = selectNationViewModel
        }
    }

    override fun onBindViewHolder(holder: NationRvHolder, position: Int) {
        holder.setUiState(nationItems[position])
    }

    override fun getItemCount(): Int {
        return nationItems.size
    }

    fun setNationList(nationItems: ArrayList<NationItem>) {
        this.nationItems = nationItems
        notifyDataSetChanged()
    }
}

class NationRvHolder(val binding: ItemNationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun setUiState(nationUiState: NationItem) {
        binding.nationItem = nationUiState
    }
}