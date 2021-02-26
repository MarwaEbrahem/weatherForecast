package com.mad41.weatherForecast.dataLayer.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Daily(

    val clouds: Double,
    val dew_point: Double,
    val dt: Double,
    val feels_like: FeelsLike,
    val humidity: Double,
    val pop: Double,
    val pressure: Double,
    val rain: Double,
    val sunrise: Double,
    val sunset: Double,
    val temp: Temp,
    val uvi: Double,
    val weather: List<WeatherXX>,
    val wind_deg: Double,
    val wind_speed: Double
)