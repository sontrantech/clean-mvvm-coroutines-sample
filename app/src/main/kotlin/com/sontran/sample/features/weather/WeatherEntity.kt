package com.sontran.sample.features.weather

import com.google.gson.annotations.SerializedName

/**
 * Data Model
 *
 * Class which provides a model for list of forecastEntity days
 * @constructor Sets all properties of the forecastEntity
 * @property timezone
 * @property currentEntity
 * @property forecastDayEntities
 */
data class WeatherEntity(
        @SerializedName("timezone") val timezone: String,
        @SerializedName("current") val currentEntity: CurrentEntity,
        @SerializedName("daily") val forecastDayEntities: List<ForecastDayEntity>
) {

    companion object {
        fun empty() = WeatherEntity("", CurrentEntity.empty(), emptyList())
    }

    /**
     * Class which provides a model for forecastDayEntity
     * @constructor Sets all properties of the forecastDayEntity
     * @property timestamp is the time count from the beginning of 1970 Jan, 01 by second
     * @property temp temperature in Kelvin for the forecastDayEntity
     */
    data class ForecastDayEntity(@SerializedName("dt") val timestamp: Long,
                                 @SerializedName("temp") val temp: TempEntity) {

        /**
         * Class which provides a model for TempEntity
         * @constructor Sets all properties of the tempEntity
         * @property morningTemperature is the morning temperature in Kelvin
         */
        data class TempEntity(@SerializedName("morn") val morningTemperature: Float)
    }

    /**
     * Class which provides a model for CurrentEntity
     * @constructor Sets all properties of the currentEntity
     * @property timestamp is the time count from the beginning of 1970 Jan, 01 by second
     * @property temp temperature in Kelvin for the currentEntity
     */
    data class CurrentEntity (@SerializedName("dt") val timestamp: Long,
                         @SerializedName("temp") val temp: Float){

        companion object {
            fun empty() = CurrentEntity(0L, 0F)
        }
    }

}