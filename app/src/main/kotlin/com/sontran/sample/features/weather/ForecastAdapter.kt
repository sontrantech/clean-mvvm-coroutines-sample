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

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sontran.sample.R
import com.sontran.sample.core.extension.*
import kotlinx.android.synthetic.main.row_weather.view.*
import javax.inject.Inject
import kotlin.properties.Delegates


class ForecastAdapter
@Inject constructor() : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    internal var collection: List<WeatherDisplay.ForecastDayDisplay>
            by Delegates.observable(emptyList()) { _, _, _ ->
                notifyDataSetChanged()
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.row_weather))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
            viewHolder.bind(collection[position])

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(foreCastDay: WeatherDisplay.ForecastDayDisplay) {
            itemView.tvTemperature.text = itemView.context.getString(R.string.current_temp,
                    foreCastDay.tempDisplay.averageInCelsius.minimumFractionDigits())
            val milliseconds = foreCastDay.timestamp.unitToMilliUnit()
            itemView.tvDate.text = getDateTime(milliseconds, FULL_MOTH_FORMAT)
        }
    }
}
