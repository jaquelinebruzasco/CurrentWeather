package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.databinding.ItemDailyBinding
import com.jaquelinebruzasco.currentweather.domain.model.DailyInfoModel
import com.jaquelinebruzasco.currentweather.ui.convertToDate
import com.jaquelinebruzasco.currentweather.ui.loadIcon

class DailyAdapter(
    private val list: MutableList<DailyInfoModel>
) : RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DailyViewHolder(
        ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DailyAdapter.DailyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class DailyViewHolder(private val binding: ItemDailyBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(dailyData: DailyInfoModel) {
                    val tempMaxMin = "${dailyData.temp.max}º | ${dailyData.temp.min}º"
                    binding.apply {
                        tvDayDaily.text = dailyData.dayTime.convertToDate()
                        tvDescriptionDaily.text = dailyData.weather[0].description
                        loadIcon(
                            imageView = ivDaily,
                            code = dailyData.weather[0].icon
                        )
                        tvMaxMinDaily.text = tempMaxMin
                    }
                }

            }
}