package com.sontran.sample.core.extension

fun Float.minimumFractionDigits() = String.format("%d", this.toInt())

fun Long.unitToMilliUnit() = this * 1000
