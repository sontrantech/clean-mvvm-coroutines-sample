package com.sontran.sample.core.extension

import com.sontran.sample.core.constant.DefaultValue.DEFAULT_LOCALE
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date = SimpleDateFormat("yyyy-MM-dd", DEFAULT_LOCALE).parse(this)
