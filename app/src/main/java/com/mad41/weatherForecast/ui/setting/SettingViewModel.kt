package com.mad41.weatherForecast.ui.setting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.google.android.gms.location.*
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather
import com.mad41.weatherForecast.dataLayer.repository
import kotlinx.coroutines.launch
import java.util.*


class SettingViewModel(application: Application) : AndroidViewModel(application) {
    var latitude = 0.0
    var longitude = 0.0
    lateinit var SP: SharedPreferences
    var App = application
    private val context = getApplication<Application>().applicationContext
    val addressLiveData = MutableLiveData<String>()
    val existLiveData = MutableLiveData<Boolean>()
    val locationResultLiveData = MutableLiveData<String>()
    var mFusedLocationClient: FusedLocationProviderClient
    private val newRepo : repository
    init {
        newRepo = repository()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(App)
        SP = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun writeAddressInSharedPreference(latLon: String, address: String) {
        var editor = SP.edit()
        editor.putString("LatLng_Map", latLon)
        editor.putString("Address_Map", address)
        editor.commit()
    }

    fun writeFavInSharedPreference(fav: Boolean) {
        var editor = SP.edit()
        editor.putBoolean("Fav_State", fav)
        editor.commit()
    }

    fun getTheAddress(latitude: Double, longitude: Double){
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = geoCoder.getFromLocation(latitude, longitude, 1)
        if(address!=null)
        addressLiveData.postValue(address[0].getAddressLine(0).toString())
    }

    fun addFavLocationToRoom(address:String , lat : Double , lon : Double){
        viewModelScope.launch {
            newRepo.insertFavLocToRoom(context, favLocation(address,lat, lon))
        }
    }
    fun FavLocationIsExist(address:String ){
        viewModelScope.launch {
            existLiveData.postValue(newRepo.FavLocFromIsExistInRoom(context,address))
        }
    }
  @SuppressLint("MissingPermission")
  fun getLastLocation() {
      mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
          val location = task.result
          if (location == null) {
              requestNewLocationData()
          } else {
              latitude = location.latitude
              longitude = location.longitude
              locationResultLiveData.postValue("$latitude , $longitude")
          }
      }
  }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(App)
        mFusedLocationClient.requestLocationUpdates(
            locationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            locationResultLiveData.postValue("$latitude , $longitude")
        }
    }

    public fun CheckPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else false
    }

    public fun isLocationEnabled(): Boolean {
        val locationManager = App.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}