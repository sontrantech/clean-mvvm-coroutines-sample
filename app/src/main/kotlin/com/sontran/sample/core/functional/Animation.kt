package com.sontran.sample.core.functional

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation


val rotateAnimation: RotateAnimation = RotateAnimation(0f, 360f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f).apply {
    interpolator = LinearInterpolator()
    duration = 1000
    repeatCount = Animation.INFINITE
}

val slide_up = TranslateAnimation(0f, 0f, 700f, 0f).apply {
    duration = 1000
}