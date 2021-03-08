package com.mad41.weatherForecast.dataLayer.local.fav_Location

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.weather
import com.mad41.weatherForecast.dataLayer.local.weatherData.allWeatherDao
import com.mad41.weatherForecast.dataLayer.local.weatherData.weatherDatabase

@Database(
    entities = [favLocation::class],
    version = 1
)
abstract class favLocDatabase : RoomDatabase() {
    abstract fun getFavLocDao(): favLocDao

    companion object {
        @Volatile
        private var instance: favLocDatabase? = null

        fun getInstance(context: Context): favLocDatabase {
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
                favLocDatabase::class.java,
                "favLocation.db"
            )
                .build()
    }
}