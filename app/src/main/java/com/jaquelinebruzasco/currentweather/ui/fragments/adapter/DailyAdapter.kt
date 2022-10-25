package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.R
import com.jaquelinebruzasco.currentweather.databinding.ItemDailyBinding
import com.jaquelinebruzasco.currentweather.domain.remote.model.DailyInfoModel
import com.jaquelinebruzasco.currentweather.ui.convertToDate
import com.jaquelinebruzasco.currentweather.ui.loadIcon

class DailyAdapter: RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    private lateinit var context : Context

    var list: List<DailyInfoModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        context = parent.context
        return DailyViewHolder(
            ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DailyAdapter.DailyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class DailyViewHolder(private val binding: ItemDailyBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(dailyData: DailyInfoModel) {
                    val tempMaxMin = context.resources.getString(R.string.temp_symbol_max_min, dailyData.temp.max.toInt(), dailyData.temp.min.toInt())
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