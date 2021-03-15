package com.mad41.weatherForecast.ui.alarm

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.alarmModel.alarm
import com.mad41.weatherForecast.databinding.FragmentAlarmBinding
import com.mad41.weatherForecast.ui.alarm.alarmImp.AlarmService
import java.text.SimpleDateFormat
import java.util.*


class AlarmFragment : Fragment()  {
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var  binding : FragmentAlarmBinding

    lateinit var alarmService: AlarmService
    var event:String= ""
    private lateinit var AlarmAdapter :alarmAdapter
    private lateinit var listner: alarmListner
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        binding = FragmentAlarmBinding.inflate(layoutInflater)
        val root = binding.root
        alarmViewModel.getAlarmFromRoom()
        alarmViewModel.alarmFromRoomLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                        setupRecycleView(it)
                    }
                }
                is Resource.Error -> {
                   Toast.makeText(context , "error in room ", Toast.LENGTH_LONG).show()
                }
            }
        })
        if(alarmViewModel.getAlarmStateFromSharedPreference()){
            binding.switch1.isChecked = true
            binding.switch1.text = resources.getString(R.string.alarm_on)
        }
        listner = object : alarmListner {
            override fun DeleteLocation(id: Int , requestCode : Int) {
                alarmViewModel.deleteAlarmFromRoom(id)
                alarmViewModel.deleteFromRoomLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        notifyRecycleViewWithChanage()
                })
                alarmService.cancel(requestCode)
            }
        }

        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.switch1.text = resources.getString(R.string.alarm_on)
                alarmViewModel.writeAlarmStateInSharedPreference(true)
            } else {
                binding.switch1.text = resources.getString(R.string.alarm_off)
                alarmViewModel.writeAlarmStateInSharedPreference(false)
            }

        }
        alarmService = AlarmService(
            requireContext()
        )
        binding.openDialog.setOnClickListener{
            setAlarmDialog().show(requireFragmentManager(), setAlarmDialog.TAG)
        }
        return root
    }
    private fun setupRecycleView(it: List<alarm>){
        AlarmAdapter = alarmAdapter(it , context , listner)
        binding.alarmRecycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AlarmAdapter
        }
    }
    public fun notifyRecycleViewWithChanage() {
        alarmViewModel.getAlarmFromRoom()
        alarmViewModel.alarmFromRoomLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                        AlarmAdapter.dataChange(it)
                    }
                }
            }
        })
    }
}


