package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.R
import com.jaquelinebruzasco.currentweather.databinding.ItemHourlyBinding
import com.jaquelinebruzasco.currentweather.domain.model.HourlyInfoModel
import com.jaquelinebruzasco.currentweather.ui.convertToHour
import com.jaquelinebruzasco.currentweather.ui.loadIcon

class HourlyAdapter: RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    private lateinit var context : Context

    var list: List<HourlyInfoModel> = listOf()

        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        context = parent.context
        return HourlyViewHolder(
            ItemHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HourlyAdapter.HourlyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class HourlyViewHolder(private val binding: ItemHourlyBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(hourlyData: HourlyInfoModel) {
                    val tempHourly =  context.resources.getString(R.string.temp_symbol, hourlyData.temp.toInt())
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