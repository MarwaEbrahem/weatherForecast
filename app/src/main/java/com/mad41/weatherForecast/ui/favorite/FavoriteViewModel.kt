package com.mad41.weatherForecast.ui.favorite

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
import com.mad41.weatherForecast.dataLayer.repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    val favLocFromRoomLiveData = MutableLiveData<Resource<List<favLocation>>>()
    val  deleteLocLiveData = MutableLiveData<Boolean>()
    var SP: SharedPreferences
    private val newRepo : repository
    init {
        SP = PreferenceManager.getDefaultSharedPreferences(context)
        newRepo = repository()
    }
    fun getFavLocationFromRoom(){
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
    fun deleteFavLocationFromRoom(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            newRepo.deleteFavLocFromRoom(context!!, address)
            deleteLocLiveData.postValue(true)
        }
    }
    fun writeFavInSharedPreference(address: String,LatLong: String) {
        var editor = SP.edit()
        editor.putString("LatLng_Map", LatLong)
        editor.putString("Address_Map", address)
        editor.putBoolean("Fav_State", true)
        editor.commit()
    }
    fun writeFavInSharedPreference(fav: Boolean) {
        var editor = SP.edit()
        editor.putBoolean("Fav_State", fav)
        editor.commit()
    }
    fun getAddressFromSharedPreference(): String? {
        return SP.getString("Address_Map","null")
    }


}