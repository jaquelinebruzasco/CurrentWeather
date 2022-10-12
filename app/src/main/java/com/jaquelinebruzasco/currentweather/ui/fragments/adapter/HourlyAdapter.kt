package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.databinding.ItemHourlyBinding
import com.jaquelinebruzasco.currentweather.domain.model.HourlyInfoModel
import com.jaquelinebruzasco.currentweather.ui.convertToHour
import com.jaquelinebruzasco.currentweather.ui.loadIcon

class HourlyAdapter(
    private val list: MutableList<HourlyInfoModel>
) : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HourlyViewHolder(
        ItemHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: HourlyAdapter.HourlyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class HourlyViewHolder(private val binding: ItemHourlyBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(hourlyData: HourlyInfoModel) {
                    val tempHourly = "${hourlyData.temp}º"
                    binding.apply {
                        tvTimeHourly.text = hourlyData.dayTime.convertToHour()
                        loadIcon(
                            imageView = ivIconHourly,
                            code = hourlyData.weather[0].icon
                        )
                        tvTempHourly.text = tempHourly
                    }
                }
            }
}