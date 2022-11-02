package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.databinding.ItemAddLocationsBinding
import com.jaquelinebruzasco.currentweather.databinding.ItemLocationsBinding
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel

class MyLocationsAdapter(
    val action: (LocationResponseModel) -> Unit,
    val actionAddLocation: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: MutableList<LocationResponseModel> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == 1) {
            AddLocationViewHolder(
                ItemAddLocationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            MyLocationViewHolder(
                ItemLocationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyLocationViewHolder) {
            list[position].let { holder.bindLocation(it) }
        } else if (holder is AddLocationViewHolder) {
            holder.bindAddLocation()
        }
    }

    override fun getItemCount() = list.size + 1

    override fun getItemViewType(position: Int) : Int {
       return if (position == itemCount) 1 else 0
    }

    inner class MyLocationViewHolder(private val binding: ItemLocationsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindLocation(data: LocationResponseModel) {
            binding.apply {
                tvLocation.text = data.locationName
            }
        }
    }

    inner class AddLocationViewHolder(private val binding: ItemAddLocationsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindAddLocation() {
            binding.root.setOnClickListener { actionAddLocation.invoke() }
        }
    }
}