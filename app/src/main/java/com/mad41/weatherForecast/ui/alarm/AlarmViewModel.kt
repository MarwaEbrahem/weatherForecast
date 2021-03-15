package com.mad41.weatherForecast.ui.alarm

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.alarmModel.alarm
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.Hourly
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather
import com.mad41.weatherForecast.dataLayer.repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    val alarmFromRoomLiveData = MutableLiveData<Resource<List<alarm>>?>()
    val weatherFromRoomLiveData = MutableLiveData<Resource<weather>>()
    val deleteFromRoomLiveData = MutableLiveData<Boolean>()
    var SP: SharedPreferences
    private val newRepo : repository
    init {
        SP = PreferenceManager.getDefaultSharedPreferences(context)
        newRepo = repository()
    }
    fun getAlarmFromRoom(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = newRepo.getAlarmFromRoom(context)
            alarmFromRoomLiveData.postValue(handleGetAlarmFromRoom(result)!!)
        }
    }
     fun handleGetAlarmFromRoom(result: List<alarm>): Resource<List<alarm>>? {
        if (result != null) {
            val currentTime = Calendar.getInstance().getTime();
            val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm a")
            val answer: String = formatter.format(currentTime)
            val R = result.filter {it.DateAndTimeFrom > answer}
            return Resource.Success(R)
        }
        return Resource.Error("Room is empty")
    }
    fun addAlarmToRoom(
        DateAndTimeFrom: String,
        DateAndTimeTo: String,
        event: String,
        requestCode: Int
    ){
        viewModelScope.launch {
            newRepo.insertAlarmToRoom(context, alarm(DateAndTimeFrom ,DateAndTimeTo ,event,requestCode))
        }
    }
    fun getWeatherFromRoom(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = newRepo.getWeatherFromRoom(context)
            weatherFromRoomLiveData.postValue(handleGetWeatherFromRoom(result)!!)
        }
    }
    fun deleteAlarmFromRoom(id :Int){
        viewModelScope.launch(Dispatchers.IO) {
            newRepo.deleteAlarmToRoom(context,id)
            deleteFromRoomLiveData.postValue(true)
        }
    }

    private fun handleGetWeatherFromRoom(result: weather): Resource<weather>? {
        if (result != null) {
            return Resource.Success(result)
        }
        return Resource.Error("Room is empty")
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        val formatter = SimpleDateFormat(dateFormat)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
    fun getDateTime(s: String , pattern:String): String {
        try {
            val sdf = SimpleDateFormat(pattern)
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
    fun convertAndCheck(
        date: String,
        startTime: String,
        endTime: String
    ): Boolean {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
        var convertedDate: Date?
        var convertedDate2: Date?
        var convertedDate3: Date?
        try {
            convertedDate = dateFormat.parse(date)
            convertedDate2 = dateFormat.parse(startTime)
            convertedDate3 = dateFormat.parse(endTime)
            if ((convertedDate2.before(convertedDate)&&convertedDate3.after(convertedDate))
                ||convertedDate2.equals(convertedDate)||convertedDate3.equals(convertedDate)) {
                return true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }
    fun search11(
        alarmHours: List<Hourly>,
        startTime: String,
        endTime: String,
        event: String
    ): Hourly? {
        var i: Int = 0
        while (i < alarmHours.size) {
            var timeInList = getDateTime(alarmHours.get(i).dt.toString(), "MM/dd/yyyy hh:mm:ss")
            if (convertAndCheck(timeInList!!, startTime!!, endTime)
                && alarmHours.get(i).weather.get(0).main.equals(event))
            {
                return alarmHours.get(i)
            } else {
                i++
            }
        }
        return null
    }
    fun writeAlarmStateInSharedPreference(state: Boolean) {
        var editor = SP.edit()
        editor.putBoolean("Alarm_state", state)
        editor.commit()
    }
    fun getAlarmStateFromSharedPreference(): Boolean {
        return SP.getBoolean("Alarm_state",false)
    }

}