package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.databinding.HomeContainerInfoBinding
import com.jaquelinebruzasco.currentweather.domain.model.CurrentInfoModel

class InfoAdapter(
    private val list: MutableList<CurrentInfoModel>
) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InfoViewHolder(
        HomeContainerInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class InfoViewHolder(private val binding: HomeContainerInfoBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(infoData: CurrentInfoModel) {
                    binding.apply {
                        tvUviInfo.text = infoData.uvi.toString()
                        tvWindInfo.text = infoData.wind.toString()
                        tvHumidityInfo.text = infoData.humidity.toString()
                    }
                }
            }
}