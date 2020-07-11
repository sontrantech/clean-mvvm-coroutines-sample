package com.sontran.sample.core.extension

import com.sontran.sample.core.constant.DefaultValue.DEFAULT_LOCALE
import java.text.SimpleDateFormat
import java.util.*

const val FULL_MOTH_FORMAT = "EEEE, MMMM dd, yyyy"

fun getDateTime(timestamp: Long, format: String): String? {
    return try {
        val sdf = SimpleDateFormat(format, DEFAULT_LOCALE)
        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

