package com.mad41.weatherForecast.dataLayer

import android.content.Context
import com.mad41.weatherForecast.dataLayer.entity.alarmModel.alarm
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather
import com.mad41.weatherForecast.dataLayer.local.dataBase.weatherForecastDatabase
import com.mad41.weatherForecast.dataLayer.remote.Constants.Companion.API_KEY
import com.mad41.weatherForecast.dataLayer.remote.RetrofitInstance


 public class repository {
      //retrofit
     public suspend fun retrofitWeatherCall(lat:Double,long:Double,language:String,units : String) =
        RetrofitInstance.getweatherinstance().getWeather(lat,long,"minutely",units,language,API_KEY)

     //weather
     public suspend fun insertWeatherToRoom(context: Context ,weather: weather){
         weatherForecastDatabase.getInstance(context).getWeatherDao().weatherInsert(weather)
     }
     public suspend fun getWeatherFromRoom(context: Context) =
         weatherForecastDatabase.getInstance(context).getWeatherDao().getWeatherInfo()

     //favLocation
     public suspend fun insertFavLocToRoom(context: Context ,favLocation: favLocation){
         weatherForecastDatabase.getInstance(context).getFavLocDao().favLocInsert(favLocation)
     }
     public suspend fun getFavLocFromRoom(context: Context) =
         weatherForecastDatabase.getInstance(context).getFavLocDao().getFavLocations()

     public suspend fun deleteFavLocFromRoom(context: Context,address : String){
         weatherForecastDatabase.getInstance(context).getFavLocDao().deleteLocation(address)
     }
     public suspend fun FavLocFromIsExistInRoom(context: Context,address : String)=
         weatherForecastDatabase.getInstance(context).getFavLocDao().exists(address)

     //alarm
     public suspend fun insertAlarmToRoom(context: Context ,alarm: alarm){
         weatherForecastDatabase.getInstance(context).getAlarmDao().alarmInsert(alarm)
     }
     public suspend fun getAlarmFromRoom(context: Context) =
         weatherForecastDatabase.getInstance(context).getAlarmDao().getAlarm()

     public suspend fun deleteAlarmToRoom(context: Context ,id : Int){
         weatherForecastDatabase.getInstance(context).getAlarmDao().deleteAlarm(id)
     }
 }
