package com.mad41.weatherForecast.dataLayer.local.alarm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mad41.weatherForecast.dataLayer.entity.alarmModel.alarm
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
@Dao
interface alarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun alarmInsert(alarm:alarm)

    @Query("SELECT * FROM weatherAlarm")
    fun getAlarm(): List<alarm>

    @Query("DELETE FROM weatherAlarm WHERE alarm_id = :id")
    suspend fun deleteAlarm(id : Int)
}