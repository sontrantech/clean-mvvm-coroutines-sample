package com.sontran.sample.features.weather

import com.sontran.sample.features.weather.WeatherConstant.KELVIN_CELSIUS_DIFFERENCE


/**
 * Presentation Model
 */
data class WeatherDisplay
private constructor(val timezone: String,
                    val currentDisplay: CurrentDisplay,
                    val forecastDaysDisplay: MutableList<ForecastDayDisplay>) {

    data class CurrentDisplay private constructor(val timestamp: Long, val temp: Float) {
        val tempInCelsius = temp - KELVIN_CELSIUS_DIFFERENCE

        companion object {
            fun from(current: Weather.Current): CurrentDisplay {
                return CurrentDisplay(current.timestamp, current.temp)
            }
        }
    }

    data class ForecastDayDisplay private constructor(val timestamp: Long, val tempDisplay: TempView) {
        data class TempView(val average: Float) {
            val averageInCelsius = average - KELVIN_CELSIUS_DIFFERENCE

            companion object {
                fun from(temp: Weather.ForecastDay.Temp): TempView {
                    return TempView(temp.morningTemp)
                }
            }
        }

        companion object {
            private fun from(forecastDay: Weather.ForecastDay): ForecastDayDisplay {
                return ForecastDayDisplay(forecastDay.timestamp, TempView.from(forecastDay.temp))
            }

            fun fromListOf(forecastDays: List<Weather.ForecastDay>): MutableList<ForecastDayDisplay> {
                return forecastDays.map { from(it) }.toMutableList()
            }
        }
    }

    companion object {
        fun from(weather: Weather): WeatherDisplay {
            return WeatherDisplay(weather.timezone,
                    CurrentDisplay.from(weather.current),
                    ForecastDayDisplay.fromListOf(weather.forecastDays))
        }
    }
}