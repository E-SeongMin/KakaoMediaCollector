package com.collector.presentation.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {
    fun formatDateTime(dateTime: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateTime)
            return outputFormat.format(date)
        } catch (e: Exception) {
            return dateTime
        }
    }
}