package com.sontran.sample.features.weather

data class WeatherRequest(
        val apiKey: String,
        val latitude: Double,
        val longitude: Double,
        val exclude: String
)