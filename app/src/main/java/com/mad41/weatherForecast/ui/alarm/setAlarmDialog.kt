package com.mad41.weatherForecast.ui.alarm

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Hourly
import com.mad41.weatherForecast.ui.alarm.alarmImp.AlarmService
import com.mad41.weatherForecast.ui.alarm.alarmImp.RandomUtil
import java.util.*

class setAlarmDialog : DialogFragment() {

    companion object {
        const val TAG = "DialogWithData"
    }
    private lateinit var viewModel: AlarmViewModel
    lateinit var submit: Button
    lateinit var spinner: Spinner
    lateinit var datePacker: ImageView
    lateinit var timePacker: ImageView
    lateinit var timeTxt: TextView
    lateinit var dateTxt: TextView
    lateinit var alarmService: AlarmService
    lateinit var alarmHours: List<Hourly>
    var x:Long = 0
    var startTime:String = ""
    var y:Long = 0
    var endTime:String= ""
    var event:String= ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.set_alarm_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AlarmViewModel::class.java)
        alarmService = AlarmService(
            requireContext()
        )
        submit = view.findViewById(R.id.submit)
        spinner = view.findViewById(R.id.spinner2)
        datePacker = view.findViewById(R.id.DatePacker)
        timePacker = view.findViewById(R.id.timePacker)
        timeTxt = view.findViewById(R.id.timeTxt)
        dateTxt = view.findViewById(R.id.DataTxt)
        val events = resources.getStringArray(R.array.events)
        val myAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,events)
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(myAdapter)
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View, i: Int, l: Long
            ) {
                var E = resources.getStringArray(R.array.events)
                event = E[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
        viewModel.getWeatherFromRoom()
        viewModel.weatherFromRoomLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                        alarmHours = it.hourly
                    }
                }
                is Resource.Error -> {

                }
            }
        })
      setupClickListeners(view)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners(view: View) {
        timePacker.setOnClickListener {
            setAlarm {
                x = it
                startTime = viewModel. getDate(it , "MM/dd/yyyy hh:mm:ss").toString()
                timeTxt.text = viewModel.getDate(it , "dd-MM-yyyy hh:mm a")
            }
        }
        datePacker.setOnClickListener{
            setAlarm {
                y = it
                endTime =  viewModel.getDate(it , "MM/dd/yyyy hh:mm:ss").toString()
                dateTxt.text = viewModel.getDate(it , "dd-MM-yyyy hh:mm a")
            }

        }
        submit.setOnClickListener {
            val result = viewModel.search11(alarmHours,startTime,endTime,event)
            if (result != null) {
                alarmService.setExactAlarm(x, result.weather.get(0).description,
                    RandomUtil.getRandomInt())
            } else {
                alarmService.setExactAlarm(x, event + " not found at this time",
                    RandomUtil.getRandomInt())
            }
            viewModel.addAlarmToRoom(timeTxt.text as String,dateTxt.text as String ,event ,
                RandomUtil.getRandomInt())
            val alarmFrag = AlarmFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,alarmFrag)?.addToBackStack(null)?.commit()
            dismiss()
        }
    }
    private fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                requireContext(),
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        context,
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}