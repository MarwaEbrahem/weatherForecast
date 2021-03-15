package com.mad41.weatherForecast.ui.alarm.alarmImp
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import android.widget.Toast

class AlarmService(private val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    fun setExactAlarm(
        timeInMillis: Long,
        event: String,
        msg : String,
        requestCode: Int
    ) {
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action =
                        Constants.ACTION_SET_EXACT
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    putExtra(Constants.ACTION_EVENT, event)
                    putExtra(Constants.ACTION_MSG, msg)
                } , requestCode
            )
        )
    }
    private fun getPendingIntent(intent: Intent , requestCode: Int) =
        PendingIntent.getBroadcast(
            context,
           requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                    )
                }
            }
        }
    }
     fun cancel(requestCode: Int){
        alarmManager?.cancel(getPendingIntent(getIntent(),requestCode))
         System.out.println("===---===---===---===--==-- From cancel")
    }
     fun setAlertAlaram(
         alarmtime: Long,
         event: String,
         msg: String,
         code: Int
     ) {
        val intentDialogueReciever = getIntent().apply {
            action = Constants.ACTION_Alert
               putExtra(Constants.ACTION_EVENT, event)
               putExtra(Constants.ACTION_MSG, msg)
        }
        val pendingIntentDialogueReciever =
            PendingIntent.getBroadcast(context, code, intentDialogueReciever, 0)
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, alarmtime, pendingIntentDialogueReciever)
        context.registerReceiver(AlarmReceiver(), IntentFilter())

    }
    private fun getIntent() = Intent(context, AlarmReceiver::class.java)
}