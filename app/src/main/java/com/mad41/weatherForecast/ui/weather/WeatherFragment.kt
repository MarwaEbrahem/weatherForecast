package com.mad41.weatherForecast.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Alert
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Daily
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Hourly
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather
import com.mad41.weatherForecast.databinding.FragmentWeatherBinding
import com.mad41.weatherForecast.ui.alarm.alarmImp.AlarmService
import java.util.*


class WeatherFragment : Fragment() {
    lateinit var binding: FragmentWeatherBinding
    private lateinit var homeViewModel: WeatherViewModel
    lateinit var alarmService: AlarmService
    lateinit var calendar : Calendar
     var values: String = ""
    lateinit var language : String
    lateinit var lis : listner
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        binding = FragmentWeatherBinding.inflate(layoutInflater)
        val root = binding.root
        alarmService = AlarmService(
            requireContext()
        )
        calendar = Calendar.getInstance()
        language = homeViewModel.getLanguageState()
        binding.today.isEnabled = false
        homeViewModel.getWeatherAPIData()
        homeViewModel.WeatherLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {
                        if (homeViewModel.getAlarmStateFromSharedPreference()) {
                            if (it.alerts != null) {
                                sendAlarm(it.alerts)
                            }
                            else{
                                alarmService.setExactAlarm(calendar.timeInMillis,"You don't have alerts in current weather",1)
                            }
                        }
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })
        homeViewModel.weatherFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.welcome!!.visibility = View.GONE
                    binding.welcomeImg!!.visibility = View.GONE
                    binding.msg!!.visibility  = View.GONE
                    binding.weatherLiner!!.visibility = View.VISIBLE
                    it.data?.daily?.let { it1 -> displayDailyWeatherToRecycleView(it1) }
                    it.data?.hourly?.let { it2 -> displayHourlyWeatherToRecycleView(it2) }
                     displayCurrentWeatherToCardView(it.data)
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })
        homeViewModel.goToSettingLiveData.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.welcome!!.visibility = View.VISIBLE
                binding.welcomeImg!!.visibility = View.VISIBLE
                binding.msg!!.visibility  = View.VISIBLE
                binding.weatherLiner!!.visibility = View.GONE
               // Toast.makeText(getActivity(), "set Location you want in setting section", Toast.LENGTH_SHORT).show()
            }
        })
        binding.today.setOnClickListener(View.OnClickListener {
            showHourlyInfo()
        })
        binding.days7.setOnClickListener(View.OnClickListener {
            showDailyInfo()
        })
        lis = object :listner{
            override fun ConvertDateAndTime(s : String ,pattern: String): String? {
               return homeViewModel.getDateTime(s , pattern ,language)
            }

        }
        return root
    }

    private fun displayHourlyWeatherToRecycleView(it2: List<Hourly>) {
        setupHourlyRecycleView(it2)
    }

    private fun setupHourlyRecycleView(it2: List<Hourly>) {
        var hourlyAdapter = hourlyAdapter(it2 , requireContext() , lis)
        binding.hourly.apply {
            val mLayoutManager = LinearLayoutManager(context)
            mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = mLayoutManager
            adapter = hourlyAdapter
        }
    }

    private fun showDailyInfo() {
        binding.days7.isEnabled = false
        binding.today.isEnabled = true
        binding.daily.visibility = View.VISIBLE
        binding.hourly.visibility = View.GONE
    }

    private fun showHourlyInfo() {
        binding.today.isEnabled = false
        binding.days7.isEnabled = true
        binding.daily.visibility = View.GONE
        binding.hourly.visibility = View.VISIBLE
      //  binding.currentInfoCard.visibility = View.VISIBLE
    }

    private fun displayCurrentWeatherToCardView(current: weather?) {
        binding.country.text = homeViewModel.getAddress(current!!.lat,current!!.lon , requireContext(),language)
        binding.date.text =homeViewModel.getDateTime(current!!.current.dt.toString(),"EEE, dd/MM/yyyy" , language)
        binding.humidity.text = current!!.current.humidity.toString()
        binding.wind.text = current!!.current.wind_speed.toString()
        binding.pressure.text = current!!.current.pressure.toString()
        binding.cloud.text = current!!.current.clouds.toString()
        binding.description.text = current!!.current.weather.get(0).description
        binding.temp.text = current!!.current.temp.toString()+"ْ"
        binding.sunrise.text =homeViewModel.getDateTime(current!!.current.sunrise.toString(),"HH:mm:ss" , language)
        binding.sun.text = homeViewModel.getDateTime(current!!.current.sunset.toString(),"HH:mm:ss",language)
        Glide.with(this)
            .load("http://openweathermap.org/img/w/" + current!!.current.weather.get(0).icon + ".png")
            .into(binding.icone)

    }

    private fun displayDailyWeatherToRecycleView(data: List<Daily>) {
        if (data != null) {
            System.out.println(data.get(0).dew_point)
            setupRecycleView(data)
        }
    }

    private fun showErrorMessage(message: String?) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
        System.out.println("Error is  :  ---->  " + message)
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun setupRecycleView(data: List<Daily>) {
        var dailyAdapter = dailyAdapter(data ,requireContext() ,lis)
        binding.daily.apply {
            val mLayoutManager = LinearLayoutManager(context)
            mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = mLayoutManager
            adapter = dailyAdapter
        }
    }
    fun sendAlarm(alerts: List<Alert>) {
        for (item in iterator<Alert> { alerts }) {
            item.event
            values = values+item.event+ " from"+homeViewModel
                .getDateTime(item.start.toString(),"dd-MM-yyyy  hh:mm a","en")+" to "+
                    homeViewModel.getDateTime(item.end.toString(),"dd-MM-yyyy  hh:mm a" ,"en")+"\n"
        }
        alarmService.setExactAlarm(calendar.timeInMillis,values,1)
    }


}