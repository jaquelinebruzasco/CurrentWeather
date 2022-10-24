package com.jaquelinebruzasco.currentweather.ui

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jaquelinebruzasco.currentweather.domain.model.ApiConstants
import java.text.SimpleDateFormat
import java.util.*

fun loadIcon(
    imageView: ImageView,
    code: String
) {
    Glide.with(imageView.context)
        .load("${ApiConstants.ICON_URL}$code${ApiConstants.ICON_EXTENSION_URL}")
        .into(imageView)
}

fun Int.convertToHour(): String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(this * 1000L)

fun Int.convertToDate(): String = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(this * 1000L)

fun Int.convertToDayDateHour(): String = SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm", Locale.getDefault()).format(this * 1000L)