package com.mad41.weatherForecast.ui.weather

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.Current
import com.mad41.weatherForecast.dataLayer.entity.Daily
import com.mad41.weatherForecast.dataLayer.repository
import com.mad41.weatherForecast.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {
    lateinit var binding: FragmentWeatherBinding
    private var adapt: RecyclerView.Adapter<weatherAdapter.CurrentViewHolder>? = null
    private var layoutManag: RecyclerView.LayoutManager? = null
    private lateinit var homeViewModel: WeatherViewModel
    private lateinit var repo : repository
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(WeatherViewModel::class.java)
        repo = repository()
        binding = FragmentWeatherBinding.inflate(layoutInflater)
        val root = binding.root//inflater.inflate(R.layout.fragment_weather, container, false)
        binding.today.isEnabled = false
        context?.let { homeViewModel.getWeatherAPIData(it) }
        homeViewModel.WeatherLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {
                       // textView.text = it.current.weather.get(0).description
                       System.out.println(""+ it.current.humidity)
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
      /*  homeViewModel.CurrentFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                   // displayCurrentWeatherToRecycleView(it.data)
                   // it.data?.let { it1 -> initUI(it1) }
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })
        homeViewModel.dailyFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    it.data?.let { it1 -> displayDailyWeatherToRecycleView(it1) }
                    it.data?.let { it1 -> initUI(it1) }
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })*/
        homeViewModel.weatherFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    it.data?.let { it1 -> displayDailyWeatherToRecycleView(it1.daily) }
                    it.data?.let { it1 -> initUI(it1.daily) }
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })
        binding.today.setOnClickListener(View.OnClickListener {
            binding.today.isEnabled = false
            binding.days7.isEnabled = true
            binding.current.visibility = View.GONE
            binding.currentInfoCard.visibility = View.VISIBLE

        })
        binding.days7.setOnClickListener(View.OnClickListener {
            binding.days7.isEnabled = false
            binding.today.isEnabled = true
            binding.current.visibility = View.VISIBLE
            binding.currentInfoCard.visibility = View.GONE
        })
        return root
    }

    private fun displayDailyWeatherToRecycleView(data: List<Daily>) {
        if (data != null) {
            System.out.println(data.get(0).dew_point)
            binding.textView.text = data.get(0).dt.toString()
        }
    }

    private fun showErrorMessage(message: String?) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show()
        System.out.println("Error is  :  ---->  " + message)
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
    private fun initUI(data: List<Daily>) {
        var dailyAdapter = weatherAdapter(data)
        binding.current.apply {
            layoutManag = LinearLayoutManager(context)
            layoutManager = layoutManag
            adapt = dailyAdapter
            adapter = adapt
        }
    }

}