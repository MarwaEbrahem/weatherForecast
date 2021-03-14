package com.mad41.weatherForecast.ui.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Hourly

class hourlyAdapter(
    var hourlyWeather: List<Hourly>,
    context: Context?,
    listner: listner
) :
    RecyclerView.Adapter<hourlyAdapter.CurrentViewHolder>() {
    val c = context
    val l = listner
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = CurrentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_current, parent, false)
    )

    override fun getItemCount() = 24

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) {
        holder.bind(hourlyWeather.get(position) , c , l)
    }

    class CurrentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView = view.findViewById<ImageView>(R.id.weatherIcone)
        private val Date =  view.findViewById<TextView>(R.id.day)
        private val desc =  view.findViewById<TextView>(R.id.desc)
        private val Dtemp =  view.findViewById<TextView>(R.id.Dtemp)
        private val wind =  view.findViewById<TextView>(R.id.windSp)
        fun bind(
            hourlyWeather: Hourly,
            c: Context?,
            l: listner
        ) {
            Date.text =l.ConvertDateAndTime(hourlyWeather.dt.toString(),"HH:mm").toString()
            desc.text = hourlyWeather.weather.get(0).description
            Dtemp.text = hourlyWeather.temp.toString()+"ْ"
            wind.text = hourlyWeather.wind_speed.toString()
            Glide.with(c!!)
                .load("http://openweathermap.org/img/w/" + hourlyWeather.weather.get(0).icon + ".png")
                .into(imageView)

        }
    }

}