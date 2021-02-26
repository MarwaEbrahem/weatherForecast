package com.mad41.weatherForecast.ui.weather

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.Current
import com.mad41.weatherForecast.dataLayer.entity.Daily
import com.mad41.weatherForecast.dataLayer.entity.weather
import com.mad41.weatherForecast.dataLayer.repository
import kotlinx.coroutines.*
import java.io.IOException

class WeatherViewModel: ViewModel() {
    private val newRepo : repository
     init {
         newRepo = repository()
     }

    val WeatherLiveData = MutableLiveData<Resource<weather>>()
    val weatherFromRoomLiveData = MutableLiveData<Resource<weather>>()

    fun getWeatherAPIData(context: Context) =  CoroutineScope(Dispatchers.IO).launch {
        WeatherLiveData.postValue(Resource.Loading())
        try {
            if(hasInternetConnection(context)){
                val response = newRepo.retrofitWeatherCall()
                WeatherLiveData.postValue(handleGetWeatherApiData(response , context)!!)
            }else{
                WeatherLiveData.postValue(Resource.Error("No internet connection"))
            }
            val weather = newRepo.getWeatherFromRoom(context)
            weatherFromRoomLiveData.postValue(handleGetWeatherFromRoom(weather)!!)
        }catch (t:Throwable){
            when(t){
                is IOException -> WeatherLiveData.postValue(Resource.Error("Network failuar"))
                else -> WeatherLiveData.postValue(Resource.Error("Conversion Error"+t))
            }
        }
    }

    private fun handleGetWeatherFromRoom(weather: weather): Resource<weather>? {
        if(weather != null){
            return Resource.Success(weather)
        }
        return Resource.Error("Room is empty")
    }

    private suspend fun handleGetWeatherApiData(
        response: retrofit2.Response<weather>,
        context: Context
    ): Resource<weather>? {
       if(response.isSuccessful){
          response.body()?.let {
              newRepo.insertWeatherToRoom(context,it)
              return Resource.Success(it)
          }
       }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(context: Context):Boolean{
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activityNetwork = connectivityManager.activeNetwork?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activityNetwork)?:return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}