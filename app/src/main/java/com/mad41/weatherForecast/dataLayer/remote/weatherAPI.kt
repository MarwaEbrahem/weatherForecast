package com.mad41.weatherForecast.dataLayer.remote

import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather
import com.mad41.weatherForecast.dataLayer.remote.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface weatherAPI {
    @GET("data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat")
        latitute : Double = 31.415160,
        @Query("lon")
        longitute : Double = 31.811520,
        @Query("exclude")
        Exclude : String = "hourly,minutely",
        @Query("units")
        Units : String = "metric",
        @Query("lang")
        language : String = "en",
        @Query("appid")
        AppKey : String = API_KEY
    ):Response<weather>
}