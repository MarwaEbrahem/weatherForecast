package com.mad41.weatherForecast.dataLayer.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mad41.weatherForecast.dataLayer.entity.Current
import com.mad41.weatherForecast.dataLayer.entity.weather

@Database(
    entities = [weather::class],
    version = 1
)
@TypeConverters(dataConverter::class)
abstract class weatherDatabase : RoomDatabase(){
    abstract fun getWeatherDao():allWeatherDao
    companion object{
        @Volatile private var instance: weatherDatabase? = null

        fun getInstance(context: Context): weatherDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, weatherDatabase::class.java, "weather.db")
                .build()
    }
}