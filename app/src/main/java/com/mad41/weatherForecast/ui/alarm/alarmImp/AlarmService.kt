package com.mad41.weatherForecast.ui.alarm.alarmImp
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class AlarmService(private val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    fun setExactAlarm(
        timeInMillis: Long,
        event: String,
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
                } , requestCode
            )
        )
    }
    fun setRepetitiveAlarm(timeInMillis: Long , requestCode : Int) {
        setDailyAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action =
                        Constants.ACTION_SET_REPETITIVE_EXACT
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                },requestCode
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
    private fun setDailyAlarm(timeInMillis: Long, pendingIntent: PendingIntent){
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
     fun cancel(requestCode: Int){
        alarmManager?.cancel(getPendingIntent(getIntent(),requestCode))
         System.out.println("===---===---===---===--==-- From cancel")
         Log.i("alarm","===---===---===---===--==-- From cancel")
    }

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

    private fun getRandomRequestCode() =
        RandomUtil.getRandomInt()

}