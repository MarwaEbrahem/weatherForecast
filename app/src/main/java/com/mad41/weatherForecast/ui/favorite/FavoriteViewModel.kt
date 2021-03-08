package com.mad41.weatherForecast.ui.favorite

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
import com.mad41.weatherForecast.dataLayer.repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {
    val favLocFromRoomLiveData = MutableLiveData<Resource<List<favLocation>>>()
    val  openWeatherFragmentLiveData = MutableLiveData<Boolean>()
    val  deleteLocLiveData = MutableLiveData<Boolean>()
    private val newRepo : repository
    init {
        newRepo = repository()
    }
    fun getFavLocationFromRoom(context: Context?){
        viewModelScope.launch(Dispatchers.IO) {
            val result = newRepo.getFavLocFromRoom(context!!)
            favLocFromRoomLiveData.postValue(handleGetFavLocFromRoom(result)!!)
        }
    }
    private fun handleGetFavLocFromRoom(result: List<favLocation>): Resource<List<favLocation>>? {
        if (result != null) {
            return Resource.Success(result)
        }
        return Resource.Error("Room is empty")
    }
    fun openWeatherFragment(boolean: Boolean) {
       openWeatherFragmentLiveData.postValue(boolean)
    }
    fun deleteFavLocationFromRoom(context: Context?, address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            newRepo.deleteFavLocFromRoom(context!!, address)
            deleteLocLiveData.postValue(true)
        }
    }
    fun writeFavInSharedPreference(address: String,LatLong: String, context: Context) {
        lateinit var SP: SharedPreferences
        SP = PreferenceManager.getDefaultSharedPreferences(context)
        var editor = SP.edit()
        editor.putString("LatLng_Map", LatLong)
        editor.putString("Address_Map", address)
        editor.putBoolean("Fav_State", true)
        editor.commit()
    }


}