package com.mad41.weatherForecast.dataLayer

import android.content.Context
import com.mad41.weatherForecast.dataLayer.entity.Current
import com.mad41.weatherForecast.dataLayer.entity.Daily
import com.mad41.weatherForecast.dataLayer.entity.weather
import com.mad41.weatherForecast.dataLayer.local.weatherDatabase
import com.mad41.weatherForecast.dataLayer.remote.RetrofitInstance


 public class repository {

     public suspend fun retrofitWeatherCall() =
        RetrofitInstance.getweatherinstance().getWeather()

     public suspend fun insertWeatherToRoom(context: Context ,weather: weather){
         weatherDatabase.getInstance(context).getWeatherDao().weatherInsert(weather)
     }
     public suspend fun getWeatherFromRoom(context: Context) =
         weatherDatabase.getInstance(context).getWeatherDao().getWeatherInfo()
 }
