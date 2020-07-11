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
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.sontran.sample.UnitTest
import com.sontran.sample.core.exception.Failure
import com.sontran.sample.core.functional.Either
import com.sontran.sample.core.platform.NetworkHandler
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Call
import retrofit2.Response

class WeatherRepositoryTest : UnitTest() {

    private lateinit var weatherRequest: WeatherRequest
    private lateinit var givenWeatherEntity: WeatherEntity
    private lateinit var networkRepository: WeatherRepository.Network

    @Mock
    private lateinit var networkHandler: NetworkHandler
    @Mock
    private lateinit var service: WeatherService
    @Mock
    private lateinit var weatherCall: Call<WeatherEntity>
    @Mock
    private lateinit var weatherResponse: Response<WeatherEntity>

    @Before
    fun setUp() {
        networkRepository = WeatherRepository.Network(networkHandler, service)
        weatherRequest = WeatherRequest("", 1.0, 1.0, "")

        givenWeatherEntity = WeatherEntity(
                "Vietnam",
                WeatherEntity.CurrentEntity(1588900800, 300f),
                mutableListOf(WeatherEntity.ForecastDayEntity(1588930800,
                        WeatherEntity.ForecastDayEntity.TempEntity(340F))))
    }

    @Test
    fun `should return empty weather forecast by default`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { weatherResponse.body() }.willReturn(null)
        given { weatherResponse.isSuccessful }.willReturn(true)
        given { weatherCall.execute() }.willReturn(weatherResponse)
        given { service.getWeather("", 1.0, 1.0, "") }.willReturn(weatherCall)

        val weather = networkRepository.getWeather(weatherRequest)

        weather shouldEqual Either.Right(Weather.empty())
        verify(service).getWeather("", 1.0, 1.0, "")
    }

    @Test
    fun `should get weather from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { weatherResponse.body() }.willReturn(
                givenWeatherEntity)
        given { weatherResponse.isSuccessful }.willReturn(true)
        given { weatherCall.execute() }.willReturn(weatherResponse)
        given { service.getWeather("", 1.0, 1.0, "") }.willReturn(weatherCall)

        val weatherEither = networkRepository.getWeather(weatherRequest)

        val weather = Weather(
                "Vietnam",
                Weather.Current(1588900800, 300f),
                mutableListOf(Weather.ForecastDay(1588930800,
                        Weather.ForecastDay.Temp(340F))))

        weatherEither shouldEqual Either.Right(weather)
        verify(service).getWeather("", 1.0, 1.0, "")
    }

    @Test
    fun `weather service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val weather = networkRepository.getWeather(weatherRequest)

        weather shouldBeInstanceOf Either::class.java
        weather.isLeft shouldEqual true
        weather.fold({ failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `weather service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val weather = networkRepository.getWeather(weatherRequest)

        weather shouldBeInstanceOf Either::class.java
        weather.isLeft shouldEqual true
        weather.fold({ failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `weather service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { service.getWeather(any(), any(), any(), any()) }.willReturn(weatherCall)

        val either = networkRepository.getWeather(weatherRequest)

        either shouldBeInstanceOf Either::class.java
        either.isLeft shouldEqual true
        either.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

    @Test
    fun `weather request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { service.getWeather(any(), any(), any(), any()) }.willReturn(weatherCall)

        val weather = networkRepository.getWeather(weatherRequest)

        weather shouldBeInstanceOf Either::class.java
        weather.isLeft shouldEqual true
        weather.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }
}