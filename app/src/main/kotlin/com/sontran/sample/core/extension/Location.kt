package com.sontran.sample.core.extension

import android.location.Location

fun Location?.isValid(): Boolean {
    return this != null && this.latitude > 0 && this.longitude > 0
}