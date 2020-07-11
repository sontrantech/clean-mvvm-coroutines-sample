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

import com.sontran.sample.core.exception.Failure
import com.sontran.sample.core.exception.Failure.NetworkConnection
import com.sontran.sample.core.functional.Either
import com.sontran.sample.core.functional.Either.Left
import com.sontran.sample.core.platform.NetworkHandler
import com.sontran.sample.core.platform.NetworkRequest
import javax.inject.Inject

interface WeatherRepository {
    fun getWeather(request: WeatherRequest): Either<Failure, Weather>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val service: WeatherService
    ) : NetworkRequest(), WeatherRepository {

        override fun getWeather(request: WeatherRequest): Either<Failure, Weather> {
            return when (networkHandler.isConnected) {
                true -> request(service.getWeather(
                        request.apiKey, request.latitude, request.longitude, request.exclude),
                        { Weather.from(it) },
                        WeatherEntity.empty())
                false, null -> Left(NetworkConnection)
            }
        }
    }
}
