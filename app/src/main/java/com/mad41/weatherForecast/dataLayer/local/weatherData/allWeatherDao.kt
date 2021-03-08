package com.mad41.weatherForecast.dataLayer.local.weatherData

import androidx.room.*
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather

@Dao
interface allWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun weatherInsert(Weather: weather)

    @Query("SELECT * FROM allWeatherInfo")
    fun getWeatherInfo(): weather

    @Delete
    suspend fun deleteWeather(Weather: weather)
}