package com.mad41.weatherForecast.dataLayer.local

import androidx.room.*
import com.mad41.weatherForecast.dataLayer.entity.Current
import com.mad41.weatherForecast.dataLayer.entity.weather

@Dao
interface allWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun weatherInsert(Weather: weather)

    @Query("SELECT * FROM allWeatherInfo")
    fun getWeatherInfo(): weather

    @Delete
    suspend fun deleteWeather(Weather: weather)
}