package com.mad41.weatherForecast.dataLayer.remote

import com.mad41.weatherForecast.dataLayer.remote.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
       /* private val retrofit by lazy {
            val logging  = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        }
        val api by lazy {
            retrofit.create(weatherAPI::class.java)
        }*/
       fun getweatherinstance(): weatherAPI {
           return Retrofit.Builder().baseUrl(BASE_URL)
               .addConverterFactory(GsonConverterFactory.create()).build()
               .create(weatherAPI::class.java)
       }
    }
}