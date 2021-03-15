package com.mad41.weatherForecast.ui.alarm.alarmImp

import android.app.AlertDialog
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.format.DateFormat
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.ui.alarm.setAlarmDialog
import io.karn.notify.Notify

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = intent.getStringExtra(Constants.ACTION_EVENT)
        val msg = intent.getStringExtra(Constants.ACTION_MSG)
        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                buildNotification(context, event!!, msg!!)
            }
            Constants.ACTION_Alert -> {
                val intent = Intent(context, alertDialog::class.java).apply {
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(Constants.ACTION_MSG , msg)
                    putExtra(Constants.ACTION_EVENT , event)
                }
                context.startActivity(intent)
            }
        }
    }

    private fun buildNotification(context: Context, title: String, message: String) {
        Notify
            .with(context)
            .content {
                this.title = title
                text = message
            }
            .show()
    }
}