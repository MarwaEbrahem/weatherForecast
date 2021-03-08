package com.mad41.weatherForecast.dataLayer

import android.content.Context
import android.location.Address
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather
import com.mad41.weatherForecast.dataLayer.local.fav_Location.favLocDatabase
import com.mad41.weatherForecast.dataLayer.local.weatherData.weatherDatabase
import com.mad41.weatherForecast.dataLayer.remote.Constants.Companion.API_KEY
import com.mad41.weatherForecast.dataLayer.remote.RetrofitInstance


 public class repository {
      //retrofit
     public suspend fun retrofitWeatherCall(lat:Double,long:Double,language:String,units : String) =
        RetrofitInstance.getweatherinstance().getWeather(lat,long,"hourly,minutely",units,language,API_KEY)

     //weather
     public suspend fun insertWeatherToRoom(context: Context ,weather: weather){
         weatherDatabase.getInstance(context).getWeatherDao().weatherInsert(weather)
     }
     public suspend fun getWeatherFromRoom(context: Context) =
         weatherDatabase.getInstance(context).getWeatherDao().getWeatherInfo()

     //favLocation
     public suspend fun insertFavLocToRoom(context: Context ,favLocation: favLocation){
        favLocDatabase.getInstance(context).getFavLocDao().favLocInsert(favLocation)
     }
     public suspend fun getFavLocFromRoom(context: Context) =
         favLocDatabase.getInstance(context).getFavLocDao().getFavLocations()

     public suspend fun deleteFavLocFromRoom(context: Context,address : String){
         favLocDatabase.getInstance(context).getFavLocDao().deleteLocation(address)
     }

 }
