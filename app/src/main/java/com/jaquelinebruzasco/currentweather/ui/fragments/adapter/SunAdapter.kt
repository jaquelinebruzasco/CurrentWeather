package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.databinding.HomeContainerSunBinding
import com.jaquelinebruzasco.currentweather.domain.model.CurrentInfoModel
import com.jaquelinebruzasco.currentweather.ui.convertToHour

class SunAdapter(
    private val list: MutableList<CurrentInfoModel>
) : RecyclerView.Adapter<SunAdapter.SunViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SunViewHolder(
        HomeContainerSunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SunViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class SunViewHolder(private val binding: HomeContainerSunBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sunData: CurrentInfoModel) {
            binding.apply {
                tvTimeSunrise.text = sunData.sunrise.convertToHour()
                tvTimeSunset.text = sunData.sunset.convertToHour()
            }
        }
    }

}