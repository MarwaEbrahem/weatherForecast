package com.mad41.weatherForecast.dataLayer.entity.alarmModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "weatherAlarm"
)
class alarm(
    @ColumnInfo(name = "DateAndTimeFrom")
    val DateAndTimeFrom: String,
    @ColumnInfo(name = "DateAndTimeTO")
    val DateAndTimeTO: String,
    @ColumnInfo(name = "event")
    val event: String,
    @ColumnInfo(name = "requestCode")
    val requestCode: Int
){
    @PrimaryKey(autoGenerate = true)
    var alarm_id: Int = 0
}
