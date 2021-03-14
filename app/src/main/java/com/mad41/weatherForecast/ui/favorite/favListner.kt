package com.mad41.weatherForecast.ui.favorite

interface favListner {
    fun DeleteLocation(address : String)
    fun saveLocationInSharedPreference(address: String,LatLong: String)
    fun writeFavInSharedPreference(fav: Boolean)
    fun getAddressFromSharedPreference(): String?
}