package com.mad41.weatherForecast.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Daily

class weatherAdapter(var dailyWeather: List<Daily>) :
    RecyclerView.Adapter<weatherAdapter.CurrentViewHolder>() {

   /* fun updateCountries(weather: weather) {
        currentWeather.clear()
        currentWeather.addAll(weather)
        notifyDataSetChanged()
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = CurrentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_current, parent, false)
    )

    override fun getItemCount() = dailyWeather.size

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) {
        holder.bind(dailyWeather.get(position))
    }

    class CurrentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView = view.findViewById<ImageView>(R.id.imageView)
        private val currentDate =  view.findViewById<TextView>(R.id.address)
        //private val countryCapital = view.capital

        fun bind(dailyWeather: Daily) {
            currentDate.text = dailyWeather.dt.toString()
            val options = RequestOptions()
                .error(R.mipmap.ic_launcher_round)
            Glide.with(imageView.context)
                .setDefaultRequestOptions(options)
                .load(dailyWeather.weather.get(0).icon)
                .into(imageView)
        }
    }

}