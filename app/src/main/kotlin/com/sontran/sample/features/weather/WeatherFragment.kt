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

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationResult
import com.sontran.sample.R
import com.sontran.sample.core.exception.Failure
import com.sontran.sample.core.exception.Failure.NetworkConnection
import com.sontran.sample.core.exception.Failure.ServerError
import com.sontran.sample.core.extension.*
import com.sontran.sample.core.functional.rotateAnimation
import com.sontran.sample.core.functional.slide_up
import com.sontran.sample.core.platform.BaseFragment
import com.sontran.sample.core.utils.location.OnRequestLocationListener
import com.sontran.sample.core.utils.location.OneShotLocationRequestHelper
import com.sontran.sample.features.weather.WeatherConstant.SAI_GON_LATITUDE
import com.sontran.sample.features.weather.WeatherConstant.SAI_GON_LONGITUDE
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.layout_error.*
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject


class WeatherFragment : BaseFragment() {

    companion object {
        private const val EXCLUDE_WEATHER_PARTS = "minutely,hourly" // minute and hour are not required
    }

    private var selectedLatitude = SAI_GON_LATITUDE
    private var selectedLongitude = SAI_GON_LONGITUDE

    private lateinit var weatherViewModel: WeatherViewModel

    @Inject
    lateinit var forecastAdapter: ForecastAdapter

    private val onRequestLocationListener = object : OnRequestLocationListener {
        override fun onSuccess(result: LocationResult) {
            Timber.d("Location permission has granted.")
            Timber.d("Last location latitude: ${result.lastLocation.latitude}")
            Timber.d("Last location longitude: ${result.lastLocation.longitude}")

            val lastLocation = result.lastLocation
            if (lastLocation.isValid()) {
                selectedLatitude = lastLocation.latitude
                selectedLongitude = lastLocation.longitude
            }
        }

        override fun onFail() {
            Timber.d("Customer denied location permission.")
        }

        override fun onComplete() {
            Timber.d("Location request is completed")
            loadWeatherList()
        }
    }

    override fun layoutId() = R.layout.fragment_weather

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        weatherViewModel = viewModel(viewModelFactory) {
            observe(weatherLiveData, ::handleWeatherData)
            failure(failure, ::handleFailure)
        }

        if (firstTimeCreated(savedInstanceState)) {
            // Call Weather api after finished request location permission
            requestCurrentLocation()
        }
    }

    private fun requestCurrentLocation() {
        val weakFragment = WeakReference<Fragment>(this)
        OneShotLocationRequestHelper(weakFragment, onRequestLocationListener).requestCurrentLocation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        setViewsVisibility(State.INITIAL)

        //retry button
        btRetry.setOnClickListener { loadWeatherList() }

        //forecast recycler view
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(appContext, linearLayoutManager.orientation)
        ContextCompat.getDrawable(appContext, R.drawable.divider_item_decoration)?.let { dividerItemDecoration.setDrawable(it) }
        rvForecast.addItemDecoration(dividerItemDecoration)
        rvForecast.layoutManager = linearLayoutManager
        rvForecast.adapter = forecastAdapter

        // pull to refresh view
        pullToRefreshView.setOnRefreshListener {
            loadWeatherList()
        }
    }

    private fun loadWeatherList() {
        setViewsVisibility(State.LOADING)
        weatherViewModel.loadForecast(selectedLatitude, selectedLongitude, EXCLUDE_WEATHER_PARTS)
    }

    private fun handleWeatherData(weatherDisplay: WeatherDisplay?) {
        setViewsVisibility(State.SUCCEED)

        //set current temperature
        val currentTemp = weatherDisplay?.currentDisplay?.tempInCelsius
        tvCurrentTemp.text =
                if (currentTemp == null) {
                    getString(R.string.unknown)
                } else {
                    getString(R.string.current_temp, currentTemp.minimumFractionDigits())
                }

        //location
        tvLocation.text = weatherDisplay?.timezone ?: getString(R.string.unknown)

        //show forecast data
        val dailyForecastDays: MutableList<WeatherDisplay.ForecastDayDisplay> =
                weatherDisplay?.forecastDaysDisplay ?: mutableListOf()
        forecastAdapter.collection = dailyForecastDays
    }

    private fun handleFailure(failure: Failure?) {
        setViewsVisibility(State.FAILED)
        when (failure) {
            is NetworkConnection -> notify(R.string.failure_network_connection)
            is ServerError -> notify(R.string.failure_server_error)
            is WeatherFailure.WeatherNotAvailable -> notify(R.string.failure_weather_list_unavailable)
        }
    }

    private fun setViewsVisibility(state: State) {
        when (state) {
            State.INITIAL -> {
                hidePullToRefreshView()
                showLoadingView(false)
                showWeatherView(false)
                llError.invisible()
            }
            State.LOADING -> {
                showWeatherView(false)
                llError.invisible()
                showLoadingView(true)
            }
            State.SUCCEED -> {
                hidePullToRefreshView()
                showLoadingView(false)
                llError.invisible()
                showWeatherView(true)
            }
            State.FAILED -> {
                hidePullToRefreshView()
                showLoadingView(false)
                showWeatherView(false)
                llError.visible()
            }
        }
    }

    private fun hidePullToRefreshView() {
        pullToRefreshView.isRefreshing = false
    }

    private fun showWeatherView(show: Boolean) {
        if (show) {
            gWeather.visible()
            rvForecast.startAnimation(slide_up)
        } else {
            gWeather.invisible()
        }
    }

    private fun showLoadingView(show: Boolean) {
        if (show) {
            ivLoading.animation = rotateAnimation
            ivLoading.visible()
        } else {
            ivLoading.animation = null
            ivLoading.invisible()
        }
    }

    enum class State {
        INITIAL, LOADING, SUCCEED, FAILED,
    }
}
