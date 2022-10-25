package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.databinding.ItemLocationsBinding
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel

class MyLocationsAdapter(
    val action: (LocationResponseModel) -> Unit
): RecyclerView.Adapter<MyLocationsAdapter.MyLocationsViewHolder>() {

    var list: MutableList<LocationResponseModel> = mutableListOf()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyLocationsViewHolder(
        ItemLocationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyLocationsViewHolder, position: Int) {
        list[position].let { holder.bindLocations(it) }
    }

    override fun getItemCount() = list.size

    inner class MyLocationsViewHolder(private val binding: ItemLocationsBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bindLocations(data: LocationResponseModel) {
                    binding.apply {  }
                }
            }
}