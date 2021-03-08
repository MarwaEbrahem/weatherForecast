package com.mad41.weatherForecast.ui.weather

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Current
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Daily
import com.mad41.weatherForecast.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {
    lateinit var binding: FragmentWeatherBinding
    private lateinit var homeViewModel: WeatherViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        binding = FragmentWeatherBinding.inflate(layoutInflater)
        val root = binding.root
        binding.today.isEnabled = false
        context?.let { homeViewModel.getWeatherAPIData(it) }
        homeViewModel.WeatherLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {
                        System.out.println("" + it.current.humidity)
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
                    it.data?.daily?.let { it1 -> displayDailyWeatherToRecycleView(it1) }
                    it.data?.current?.let { it1 -> displayCurrentWeatherToCardView(it1) }
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })
        homeViewModel.goToSettingLiveData.observe(viewLifecycleOwner, Observer {
            if(it){
                Toast.makeText(getActivity(), "set Location you want in setting section", Toast.LENGTH_SHORT).show()
            }
        })
        binding.today.setOnClickListener(View.OnClickListener {
            showTodayInfo()
        })
        binding.days7.setOnClickListener(View.OnClickListener {
            showDailyInfo()
        })
        return root
    }

    private fun showDailyInfo() {
        binding.days7.isEnabled = false
        binding.today.isEnabled = true
        binding.current.visibility = View.VISIBLE
        binding.currentInfoCard.visibility = View.GONE
    }

    private fun showTodayInfo() {
        binding.today.isEnabled = false
        binding.days7.isEnabled = true
        binding.current.visibility = View.GONE
        binding.currentInfoCard.visibility = View.VISIBLE
    }

    private fun displayCurrentWeatherToCardView(current: Current) {
        binding.textView.text = current.dt.toString()
        binding.textView2.text = current.weather.get(0).description
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
        var dailyAdapter = weatherAdapter(data)
        binding.current.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dailyAdapter
        }
    }

}