package com.mad41.weatherForecast.dataLayer.local.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mad41.weatherForecast.dataLayer.entity.alarmModel.alarm
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather
import com.mad41.weatherForecast.dataLayer.local.alarm.alarmDao
import com.mad41.weatherForecast.dataLayer.local.fav_Location.favLocDao
import com.mad41.weatherForecast.dataLayer.local.weatherData.allWeatherDao
import com.mad41.weatherForecast.dataLayer.local.weatherData.dataConverter

@Database(
    entities = [weather::class, favLocation::class ,alarm::class],
    version = 7
)
@TypeConverters(dataConverter::class)
abstract class weatherForecastDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): allWeatherDao
    abstract fun getFavLocDao(): favLocDao
    abstract fun getAlarmDao(): alarmDao

    companion object {
        @Volatile
        private var instance: weatherForecastDatabase? = null

        fun getInstance(context: Context): weatherForecastDatabase {
            return instance
                ?: synchronized(this) {
                    instance
                        ?: buildDatabase(
                            context
                        )
                            .also { instance = it }
                }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                weatherForecastDatabase::class.java,
                "weather.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}