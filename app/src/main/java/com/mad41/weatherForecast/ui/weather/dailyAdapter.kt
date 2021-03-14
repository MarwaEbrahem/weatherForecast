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
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Daily
import java.text.SimpleDateFormat
import java.util.*

class dailyAdapter(
    var dailyWeather: List<Daily>,
    context: Context?,
    listner: listner
) :
    RecyclerView.Adapter<dailyAdapter.CurrentViewHolder>() {
    val c = context
    val L = listner
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = CurrentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_current, parent, false)
    )

    override fun getItemCount() = dailyWeather.size

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) {
        holder.bind(dailyWeather.get(position) , c , L)
    }

    class CurrentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView = view.findViewById<ImageView>(R.id.weatherIcone)
        private val Date =  view.findViewById<TextView>(R.id.day)
        private val desc =  view.findViewById<TextView>(R.id.desc)
        private val Dtemp =  view.findViewById<TextView>(R.id.Dtemp)
        private val wind =  view.findViewById<TextView>(R.id.windSp)



        fun bind(
            dailyWeather: Daily,
            c: Context?,
            l: listner
        ) {

            Date.text =l.ConvertDateAndTime(dailyWeather.dt.toString(),"EEE").toString()
            desc.text = dailyWeather.weather.get(0).description
            Dtemp.text = dailyWeather.temp.day.toString()+"ْ"
            wind.text = dailyWeather.wind_speed.toString()
            Glide.with(c!!)
                .load("http://openweathermap.org/img/w/" + dailyWeather.weather.get(0).icon + ".png")
                .into(imageView)

        }
    }

}