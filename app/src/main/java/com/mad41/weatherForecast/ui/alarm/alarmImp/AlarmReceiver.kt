package com.mad41.weatherForecast.ui.alarm.alarmImp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import io.karn.notify.Notify
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        val event = intent.getStringExtra(Constants.ACTION_EVENT)

        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                buildNotification(context, event!!, convertDate(timeInMillis))
            }
            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                setRepetitiveAlarm(
                    AlarmService(
                        context
                    )
                )
                buildNotification(context, "Set Repetitive Exact Time", convertDate(timeInMillis))
            }
        }
    }

    private fun buildNotification(context: Context, title: String, message: String) {
        Notify
            .with(context)
            .content {
                this.title = title
                text = "I got triggered at - $message"
            }
            .show()
    }
    private fun setRepetitiveAlarm(alarmService: AlarmService) {
      val cal = Calendar.getInstance().apply {
          this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(35600)
          Timber.d("Set alarm for next week same time - ${convertDate(this.timeInMillis)}")
      }
      alarmService.setRepetitiveAlarm(cal.timeInMillis, 1)
  }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()

}