package com.mad41.weatherForecast.ui.map

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.ui.setting.SettingFragment2
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var saveBtn: Button
    var fusedLocationProviderClient : FusedLocationProviderClient? = null
    var currentLocation : Location? = null
    var currentMarker : Marker? = null
    var gpsStatus: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        saveBtn = findViewById(R.id.save)
       //supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /* val mapFragment = supportFragmentManager
                 .findFragmentById(R.id.map) as SupportMapFragment
         mapFragment.getMapAsync(this)*/
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            locationEnabled()
        if(gpsStatus) {
            fetchLocation()
        }
        else{
            System.out.println("GPS is not enabled..................")
        }


        saveBtn.setOnClickListener(View.OnClickListener {
            System.out.println("Current marker is : "+currentLocation!!.latitude +"-----"+currentLocation!!.longitude)
           // val intent = Intent(this , MainActivity::class.java )
           // intent .putExtra("openF2",true)
           // overridePendingTransition(0, 0);
           // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

           // startActivity(intent);
            val fm: FragmentManager = supportFragmentManager

            val fragment: SettingFragment2 =
                fm.findFragmentById(R.id.navigation_setting) as SettingFragment2
                fragment.showAddress(currentLocation!!.latitude,currentLocation!!.longitude)
            //finish()
        })
    }
    private fun fetchLocation() {
        if( ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION ),1000)
            return
        }
        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location ->
            if (location != null){
                this.currentLocation = location
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000->if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fetchLocation()
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /* val sydney = LatLng(-34.0, 151.0)
         mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        val latlong = LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
        drawMarker(latlong)

        /*  mMap.setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener{
              override fun onMarkerDragEnd(p0: Marker?) {
                  if (currentMarker != null)
                      currentMarker?.remove()
                  val newLating = LatLng(p0?.position!!.latitude,p0?.position.longitude)
                  drawMarker(newLating)
              }

              override fun onMarkerDragStart(p0: Marker?) {

              }

              override fun onMarkerDrag(p0: Marker?) {

              }

          })*/
        /* mMap.setOnCameraIdleListener(object :GoogleMap.OnCameraIdleListener{
             override fun onCameraIdle() {
                 var lat = mMap.cameraPosition.target.latitude
                 var long = mMap.cameraPosition.target.longitude
                 if (currentMarker != null)
                     currentMarker?.remove()
                 val newLating = LatLng(lat,long)
                 drawMarker(newLating)
             }

         })*/
        mMap.setOnMapClickListener(object :GoogleMap.OnMapClickListener{
            override fun onMapClick(p0: LatLng?) {

                if (currentMarker != null)
                    currentMarker?.remove()
                val newLating = LatLng(p0!!.latitude,p0!!.longitude)
               // currentLocation!!.latitude = p0!!.latitude
               // currentLocation!!.longitude = p0!!.longitude
                drawMarker(newLating)
            }

        })

    }

    private fun drawMarker(latlong: LatLng) {
        System.out.println("pin must be drawwed")
        val markerOptions = MarkerOptions().position(latlong).title("This Location is ")
            .snippet(getTheAddress(latlong.latitude,latlong.longitude)).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,15f))
        currentMarker = mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }

    private fun getTheAddress(latitude: Double, longitude: Double): String? {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(latitude,longitude,1)
        return address[0].getAddressLine(0).toString()
    }

    private fun locationEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


}