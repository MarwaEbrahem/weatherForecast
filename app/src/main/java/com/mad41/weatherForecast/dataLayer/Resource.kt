package com.mad41.weatherForecast.dataLayer

import androidx.lifecycle.LiveData

sealed class Resource<T>(
    val data:T? = null,
    val message : String? = null
) {
    class Success<T>(data: T):Resource<T>(data)
    class Error<T>(message: String,data: T? = null):Resource<T>(data,message)
    class Loading<T>:Resource<T>()
}