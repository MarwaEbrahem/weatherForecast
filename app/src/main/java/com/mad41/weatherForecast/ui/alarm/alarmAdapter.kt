package com.mad41.weatherForecast.ui.alarm

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.dataLayer.entity.alarmModel.alarm

class alarmAdapter(
    var alarms: List<alarm>,
    context: Context?,
    listner: alarmListner
) :
    RecyclerView.Adapter<alarmAdapter.CurrentViewHolder>() {
    val c = context
    val lis = listner
    fun dataChange(alarm: List<alarm>) {
        alarms = alarm
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = CurrentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
    )

    override fun getItemCount() = alarms.size

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) {
        holder.bind(alarms.get(position), c ,lis )
    }

    class CurrentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val dataFrom = view.findViewById<TextView>(R.id.from)
        private val dataTo = view.findViewById<TextView>(R.id.to)
        private val events = view.findViewById<TextView>(R.id.Event)
        private val cancel = view.findViewById<Button>(R.id.cancel)
        fun bind(
            Alarm: alarm,
            context: Context?,
            lis: alarmListner
        ) {
            dataFrom.text = Alarm.DateAndTimeFrom
            dataTo.text = Alarm.DateAndTimeTO
            events.text = Alarm.event
            cancel.setOnClickListener {
                AlertDialog.Builder(context!!)
                    .setTitle(R.string.cancelAlarm)
                    .setMessage(R.string.TitleDeleteAlarm)
                    .setPositiveButton(
                        android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, which ->
                            lis.DeleteLocation(Alarm.alarm_id , Alarm.requestCode)
                        })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        }
    }

}