package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.R
import com.jaquelinebruzasco.currentweather.databinding.ItemEnterLocationBinding
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel

class AddLocationBSDAdapter(
    val action: (LocationResponseModel) -> Unit
): RecyclerView.Adapter<AddLocationBSDAdapter.AddLocationBSDViewHolder>() {

    private lateinit var context : Context

    var list: MutableList<LocationResponseModel> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddLocationBSDViewHolder {
        context = parent.context
        return AddLocationBSDViewHolder(
            ItemEnterLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddLocationBSDAdapter.AddLocationBSDViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class AddLocationBSDViewHolder(private val binding: ItemEnterLocationBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(location: LocationResponseModel) {
                    val stateCountry = context.resources.getString(R.string.state_country, location.locationState, location.locationCountry)
                    binding.apply {
                        tvLocation.text = location.locationName
                        tvStateCountry.text = stateCountry
                        root.setOnClickListener { action.invoke(location) }
                    }
                }
            }
}