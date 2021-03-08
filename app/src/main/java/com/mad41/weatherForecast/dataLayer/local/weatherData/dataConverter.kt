package com.mad41.weatherForecast.dataLayer.local.weatherData

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.*
import java.lang.reflect.Type


class dataConverter {
  /*  @TypeConverter // note this annotation
    fun fromWeatherXXList(DailyValues: List<WeatherXX?>?): String? {
        if (DailyValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<WeatherXX?>?>() {}.type
        return gson.toJson(DailyValues, type)
    }

    @TypeConverter // note this annotation
    fun toWeatherxxList(DailyString: String?): List<WeatherXX>? {
        if (DailyString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<WeatherXX?>?>() {}.type
        return gson.fromJson<List<WeatherXX>>(DailyString, type)
    }
    @TypeConverter // note this annotation
    fun fromFealsLike(DailyValues: FeelsLike?): String? {
        if (DailyValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<FeelsLike?>() {}.type
        return gson.toJson(DailyValues, type)
    }

    @TypeConverter // note this annotation
    fun toFealsLike(DailyString: String?): FeelsLike? {
        if (DailyString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<FeelsLike?>() {}.type
        return gson.fromJson<FeelsLike>(DailyString, type)
    }
    @TypeConverter // note this annotation
    fun fromTemp(DailyValues: Temp?): String? {
        if (DailyValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Temp?>() {}.type
        return gson.toJson(DailyValues, type)
    }

    @TypeConverter // note this annotation
    fun toTemp(DailyString: String?): Temp? {
        if (DailyString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Temp?>() {}.type
        return gson.fromJson<Temp>(DailyString, type)
    }
    @TypeConverter // note this annotation
    fun fromCWeatherXList(CurrentValues: List<WeatherX?>?): String? {
        if (CurrentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken< List<WeatherX?>?>() {}.type
        return gson.toJson(CurrentValues, type)
    }

    @TypeConverter // note this annotation
    fun toCWeatherXList(CurrentString: String?): List<WeatherX?>? {
        if (CurrentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<WeatherX?>?>() {}.type
        return gson.fromJson<List<WeatherX?>>(CurrentString, type)
    }*/
    @TypeConverter // note this annotation
    fun fromCurrent(CurrentValues: Current?): String? {
        if (CurrentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken< Current?>() {}.type
        return gson.toJson(CurrentValues, type)
    }

    @TypeConverter // note this annotation
    fun toCurrent(CurrentString: String?): Current? {
        if (CurrentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current?>() {}.type
        return gson.fromJson<Current>(CurrentString, type)
    }
    @TypeConverter // note this annotation
    fun fromDailyList(CurrentValues: List<Daily?>?): String? {
        if (CurrentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken< List<Daily?>?>() {}.type
        return gson.toJson(CurrentValues, type)
    }

    @TypeConverter // note this annotation
    fun ToDailyList(CurrentString: String?): List<Daily?>? {
        if (CurrentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily?>>(CurrentString, type)
    }
}