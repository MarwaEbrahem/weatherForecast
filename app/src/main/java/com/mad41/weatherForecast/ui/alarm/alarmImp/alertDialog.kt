package com.mad41.weatherForecast.ui.alarm.alarmImp

import android.app.AlertDialog
import android.content.DialogInterface
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mad41.weatherForecast.R

class alertDialog : AppCompatActivity() {
    var builder: AlertDialog.Builder? = null
    private var mMediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var msg: String? = intent.getStringExtra(Constants.ACTION_MSG)
        var event : String? = intent.getStringExtra(Constants.ACTION_EVENT)
        mMediaPlayer = MediaPlayer()
        mMediaPlayer = MediaPlayer.create(this, R.raw.sound1)
        mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer!!.start()
        builder = AlertDialog.Builder(this)
        builder!!.setMessage(msg).setCancelable(false).setTitle(event)
            .setNegativeButton(R.string.cancelAlarmSound,
                DialogInterface.OnClickListener { dialog, id -> //  Action for 'NO' Button
                    dialog.cancel()
                    finish()
                    mMediaPlayer!!.stop()
                })
        val alert: AlertDialog = builder!!.create()
        alert.show()
    }
}