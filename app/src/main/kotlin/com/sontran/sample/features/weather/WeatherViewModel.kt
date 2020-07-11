/**
 * Copyright (C) 2019 Son Tran Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sontran.sample.features.weather

import androidx.lifecycle.MutableLiveData
import com.sontran.sample.core.platform.BaseViewModel
import com.sontran.sample.features.weather.WeatherConstant.API_KEY
import javax.inject.Inject

class WeatherViewModel
@Inject constructor(private val getWeather: GetWeather) : BaseViewModel() {

    val weatherLiveData: MutableLiveData<WeatherDisplay> = MutableLiveData()

    fun loadForecast(latitude: Double, longitude: Double, exclude: String) {
        getWeather(WeatherRequest(API_KEY, latitude, longitude, exclude)) {
            it.fold(::handleFailure, ::handleForecastResult)
        }
    }

    private fun handleForecastResult(forecast: Weather) {
        weatherLiveData.value = WeatherDisplay.from(forecast)
    }
}