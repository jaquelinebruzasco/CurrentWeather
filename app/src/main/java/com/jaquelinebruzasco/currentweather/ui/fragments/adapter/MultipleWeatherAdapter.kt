package com.jaquelinebruzasco.currentweather.ui.fragments.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.jaquelinebruzasco.currentweather.domain.model.MultipleWeatherModel


class MultipleWeatherAdapter: AsyncListDifferDelegationAdapter<MultipleWeatherModel>(DIFF_UTIL_CALLBACK) {
    companion object {
        private val DIFF_UTIL_CALLBACK =
            object : DiffUtil.ItemCallback<MultipleWeatherModel>() {
                override fun areItemsTheSame(
                    oldItem: MultipleWeatherModel,
                    newItem: MultipleWeatherModel,
                ) = oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: MultipleWeatherModel,
                    newItem: MultipleWeatherModel,
                ) = oldItem.equals(newItem)
            }
    }

    init {
        delegatesManager
        //As a parameter of .addDelegate(), you can pass the actual adapters that will compose the recyclerview
        //you can have as many as you want
//            .addDelegate()
//            .addDelegate()
//            .addDelegate()
//            .addDelegate()
    }
}