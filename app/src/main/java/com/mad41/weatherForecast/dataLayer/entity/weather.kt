package com.mad41.weatherForecast.dataLayer.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "allWeatherInfo"
)
data class weather(
    @PrimaryKey
    val id : Int,
    @ColumnInfo(name = "current")
    val current: Current,
    @ColumnInfo(name = "daily")
    val daily: List<Daily>,
    @ColumnInfo(name = "lat")
    val lat: Double,
    @ColumnInfo(name = "lon")
    val lon: Double,
    @ColumnInfo(name = "timezone")
    val timezone: String,
    @ColumnInfo(name = "timezone_offset")
    val timezone_offset: Int
)