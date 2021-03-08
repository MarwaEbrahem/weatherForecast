package com.mad41.weatherForecast.ui.weather

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather
import com.mad41.weatherForecast.dataLayer.repository
import kotlinx.coroutines.*
import java.io.IOException

class WeatherViewModel: ViewModel() {
    lateinit var language : String
    lateinit var l : String
    var lat : Double = 0.0
    var long : Double = 0.0

    private val newRepo : repository
     init {
         newRepo = repository()
     }
    val WeatherLiveData = MutableLiveData<Resource<weather>>()
    val goToSettingLiveData = MutableLiveData<Boolean>()
    val weatherFromRoomLiveData = MutableLiveData<Resource<weather>>()

    fun getWeatherAPIData(context: Context) =  CoroutineScope(Dispatchers.IO).launch {
        WeatherLiveData.postValue(Resource.Loading())
        try {
            if(hasInternetConnection(context)){
                getSettingDetails(context)
                if(l!="null") {
                    val response = newRepo.retrofitWeatherCall(lat, long, language, "metric")
                    WeatherLiveData.postValue(handleGetWeatherApiData(response, context)!!)
                }
                else{
                    goToSettingLiveData.postValue(true)
                }
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
        return Resource.Error("you don't have storage data")
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
            val capability = connectivityManager.getNetworkCapabilities(activityNetwork)?:return false
            return when {
                capability.hasTransport(TRANSPORT_WIFI) -> true
                capability.hasTransport(TRANSPORT_CELLULAR) -> true
                capability.hasTransport(TRANSPORT_ETHERNET) -> true
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
    private fun getSettingDetails(context: Context) {
        val SP: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val languageSys = SP.getString("language", "English").toString()
        if (languageSys.equals("English")) {
            language = "en"
        } else if (languageSys.equals("Arabic")) {
            language = "ar"
        }
        l = SP.getString("LatLng_Map", "null").toString()
        if (l != "null") {
            long = l!!.split(",").get(0).toDouble()
            lat = l!!.split(",").get(1).toDouble()
        }
    }

}