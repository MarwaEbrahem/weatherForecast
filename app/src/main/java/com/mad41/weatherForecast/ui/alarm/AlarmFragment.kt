package com.mad41.weatherForecast.ui.alarm

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    val c = Calendar.getInstance()
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
               // cancelAlarm(requestCode)
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
        /*val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        binding.DatePacker!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(requireContext(),
                    dateSetListener,
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)).show()
            }
        })*/
        alarmService = AlarmService(
            requireContext()
        )
        binding.openDialog.setOnClickListener{
            setAlarmDialog().show(requireFragmentManager(), setAlarmDialog.TAG)
        }
        return root
    }


  /*  private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.DataTxt!!.text = sdf.format(c.getTime())
    }*/
  fun setDailyAlarm(callback: (Long) -> Unit){
      val cal = Calendar.getInstance()
      val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
          cal.set(Calendar.HOUR_OF_DAY, hour)
          cal.set(Calendar.MINUTE, minute)
          binding.TimeTxt.text = SimpleDateFormat("HH:mm").format(cal.time)
          System.out.println("========================================================"+cal.time)

      }
      TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
      callback(cal.timeInMillis)
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
   /* fun cancelAlarm(requestCode : Int){
        val alarmManager =
            context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent =
            PendingIntent.getBroadcast(context, requestCode, Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager!!.cancel(pendingIntent)

    }*/
}


