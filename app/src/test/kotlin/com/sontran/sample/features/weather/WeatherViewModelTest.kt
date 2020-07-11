/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
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

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.sontran.sample.AndroidTest
import com.sontran.sample.core.functional.Either
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class WeatherViewModelTest : AndroidTest() {

    private lateinit var weatherViewModel: WeatherViewModel

    @Mock
    private lateinit var getWeather: GetWeather

    @Before
    fun setUp() {
        weatherViewModel = WeatherViewModel(getWeather)
    }

    @Test
    fun `loading weather should update live data`() {
        val weatherForecast = Weather("1",
                Weather.Current(123L, 12F),
                mutableListOf(Weather.ForecastDay(456L, Weather.ForecastDay.Temp(13F)),
                        Weather.ForecastDay(789L, Weather.ForecastDay.Temp(14F))))

        given { runBlocking { getWeather.run(eq(any())) } }.willReturn(Either.Right(weatherForecast))

        weatherViewModel.weatherLiveData.observeForever {
            it!!.timezone shouldEqualTo "1"

            it.currentDisplay.timestamp shouldEqualTo 123L
            it.currentDisplay.temp shouldEqualTo 12F

            it.forecastDaysDisplay.size shouldEqualTo 2
            it.forecastDaysDisplay[0].timestamp shouldEqualTo 456L
            it.forecastDaysDisplay[0].tempDisplay.average shouldEqualTo 13F
            it.forecastDaysDisplay[1].timestamp shouldEqualTo 789L
            it.forecastDaysDisplay[1].tempDisplay.average shouldEqualTo 14F
        }

        runBlocking { weatherViewModel.loadForecast(latitude = any(), longitude = any(), exclude = "") }
    }
}