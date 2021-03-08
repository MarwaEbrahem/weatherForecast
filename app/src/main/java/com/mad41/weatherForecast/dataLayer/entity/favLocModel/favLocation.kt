package com.mad41.weatherForecast.dataLayer.entity.favLocModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favLocations"
   // primaryKeys = ["latitute", "langitute"]
)
data class favLocation (
    @PrimaryKey
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "latitute")
    val lat: Double,
    @ColumnInfo(name = "langitute")
    val lon: Double

)