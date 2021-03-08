package com.mad41.weatherForecast.ui.setting

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.ui.MainActivity
import com.mad41.weatherForecast.ui.map.MapsActivity
import java.util.*


class SettingFragment2 : PreferenceFragmentCompat() {
    var resultLiveData = MutableLiveData<Boolean>()
    var wifiManager: WifiManager? = null
    private val PLACE_PICKER_REQUEST = 999
    var TP: Preference? = null
    var fav: Preference? = null
    var fav_A: Preference? = null
    var Lp: ListPreference? = null
    var language: ListPreference? = null
    var favBtn: Preference? = null
    var PERMISSION_ID = 2
    lateinit var SP: SharedPreferences
    private lateinit var settingViewModel: SettingViewModel
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        SP = PreferenceManager.getDefaultSharedPreferences(context)
        wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        Lp = findPreference<ListPreference>("location")
        language = findPreference<ListPreference>("language")
        TP = findPreference<Preference>("map_key")
        fav = findPreference<Preference>("fav")
        fav_A = findPreference<Preference>("fav_after")
        favBtn = findPreference(getString(R.string.fav))
        favBtn!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            addToFav()
            settingViewModel.writeFavInSharedPreference(true, requireContext())
            true
        }
        Lp?.setOnPreferenceChangeListener { preference, newValue ->
            if(settingViewModel.CheckPermission()){
                if(settingViewModel.isLocationEnabled()){
                    wifiManager!!.isWifiEnabled = false
                    chooseLocation(preference, newValue as String)
                    true
                }else{
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                    false
                }
            }else{
                RequestPermission()
                false
            }
        }
        language?.setOnPreferenceChangeListener { preference, newValue ->
            chooseLanguage(preference, newValue as String)
            true
        }
       /* settingViewModel.reguestPermissionLiveData.observe(this, androidx.lifecycle.Observer {
            RequestPermission()
        })*/
        /*settingViewModel.openLocationLiveData.observe(this, androidx.lifecycle.Observer {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        })*/
        settingViewModel.locationResultLiveData.observe(this, androidx.lifecycle.Observer {
            var R =  it.split(",")
            showAddress(R.get(0).toDouble(),R.get(1).toDouble())
        })
        retrive()
    }

    private fun chooseLocation(preference: Preference?, newValue: String) {
        var item: String = newValue
        if (preference!!.key.equals("location")) {
            when (item) {
                "choose_on_map" -> {
                    //val intent = Intent(requireActivity(), MapsActivity::class.java)
                    //startActivity(intent)
                    wifiManager!!.isWifiEnabled = true
                    openPlacePicker()
                }
                "current_Location" -> {
                    wifiManager!!.isWifiEnabled = true
                    settingViewModel.getLastLocation()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun chooseLanguage(preference: Preference?, newValue: String){
        var item: String = newValue
        if (preference!!.key.equals("language")) {
            when (item) {
                "Arabic" -> {
                    setLocale(context as Activity,"ar")
                    requireActivity().recreate()
                }
                "English" -> {
                    setLocale(context as Activity,"en")
                    requireActivity().recreate()
                }
            }
        }
    }

    private fun addToFav() {
        fav_A!!.isVisible = true
        fav!!.isVisible = false
        TP!!.summary
        var R =  TP!!.title.split(",")
       System.out.println("-----------------------------------------"+R.get(0).toDouble() +"-------------"+ R.get(1).toDouble())
        settingViewModel.addFavLocationToRoom(requireContext(), TP!!.summary as String,R.get(0).toDouble(),R.get(1).toDouble())
    }

    private fun retrive() {
        var latlon = SP.getString("LatLng_Map", "null")
        var address = SP.getString("Address_Map", "null")
        var favValue = SP.getBoolean("Fav_State", false)
        if (!latlon!!.equals("null") || !address!!.equals("null")) {
            TP!!.isVisible = true
            TP!!.setSummary(address)
            TP!!.setTitle(latlon)
            if(favValue){
                fav_A?.isVisible = true
            }
            else{
                fav?.isVisible = true
            }
        }
        System.out.println("----------------------------------------------->"+latlon)
    }

    private fun openPlacePicker() {
        val builder = PlacePicker.IntentBuilder()
        try {
            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
            wifiManager!!.isWifiEnabled = true
        } catch (e: GooglePlayServicesRepairableException) {
            e.message?.let { Log.d("Exception", it) }
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.message?.let { Log.d("Exception", it) }
            e.printStackTrace()
        }
    }
    private fun RequestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(context,data)
                    place.latLng.longitude
                    showAddress(place.latLng.latitude,place.latLng.longitude)
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                settingViewModel.getLastLocation()
                System.out.println("priiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiint")
            }
        }
    }

    public fun showAddress(lat:Double , lag : Double) {
      //  val place = PlacePicker.getPlace(context, data)
        val latitude : Float = lat.toFloat()
        val longitude :Float = lag.toFloat()
        val PlaceLatLng = "$latitude , $longitude"
        settingViewModel.getTheAddress(lat,lag,requireContext())
        settingViewModel.addressLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            TP!!.isVisible = true
            TP!!.setSummary(it)
            TP!!.title = "$latitude , $longitude"
            fav_A!!.isVisible = false
            fav!!.isVisible = true
            settingViewModel.writeAddressInSharedPreference(PlaceLatLng, it!! ,requireContext())
            settingViewModel.writeFavInSharedPreference(false,requireContext())
        })

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    open fun setLocale(activity: Activity, languageCode: String?): Unit {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}