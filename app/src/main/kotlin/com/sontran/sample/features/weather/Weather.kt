package com.sontran.sample.features.weather


/**
 * Domain Model
 *
 * Class which provides a model for list of forecast days
 * @constructor Sets all properties of the forecast
 * @property current
 * @property forecastDays
 */
data class Weather(val timezone: String, val current: Current, val forecastDays: List<ForecastDay>) {

    companion object {
        fun empty(): Weather {
            return Weather("", Current.empty(), emptyList())
        }

        fun from(weatherEntity: WeatherEntity) = Weather(weatherEntity.timezone,
                Current.from(weatherEntity.currentEntity),
                ForecastDay.fromListOf(weatherEntity.forecastDayEntities))
    }

    /**
     * Class which provides a model for Daily
     * @constructor Sets all properties of the daily
     * @property timestamp is the time count from the beginning of 1970 Jan, 01 by second
     * @property temp is the temperatures in the day
     */
    data class ForecastDay(val timestamp: Long, val temp: Temp) {
        /**
         * Class which provides a model for forecast day
         * @constructor Sets all properties of the forecast day
         * @property morningTemp is the morning temperature
         */
        data class Temp(val morningTemp: Float) {
            companion object {
                fun from(tempEntity: WeatherEntity.ForecastDayEntity.TempEntity) = Temp(tempEntity.morningTemperature)
            }
        }

        companion object {
            private fun from(forecastDayEntity: WeatherEntity.ForecastDayEntity) = ForecastDay(forecastDayEntity.timestamp, Temp.from(forecastDayEntity.temp))

            fun fromListOf(forecastDayEntityList: List<WeatherEntity.ForecastDayEntity>): List<ForecastDay> {
                return forecastDayEntityList.map { from(it) }
            }
        }
    }

    /**
     * Class which provides a model for current day
     * @constructor Sets all properties of the current day
     * @property timestamp is the current timestamp
     * @property temp is the current temperature
     */
    data class Current(val timestamp: Long, val temp: Float) {
        companion object {
            fun empty(): Current {
                return Current(0L, 0F)
            }

            fun from(currentEntity: WeatherEntity.CurrentEntity) = Current(currentEntity.timestamp, currentEntity.temp)

        }
    }
}