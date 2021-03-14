package com.mad41.weatherForecast.dataLayer.local.weatherData

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mad41.weatherForecast.dataLayer.entity.weatherModel.*
import java.lang.reflect.Type


class dataConverter {
    @TypeConverter
    fun fromCurrent(CurrentValues: Current?): String? {
        if (CurrentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken< Current?>() {}.type
        return gson.toJson(CurrentValues, type)
    }

    @TypeConverter
    fun toCurrent(CurrentString: String?): Current? {
        if (CurrentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current?>() {}.type
        return gson.fromJson<Current>(CurrentString, type)
    }
    @TypeConverter
    fun fromDailyList(CurrentValues: List<Daily?>?): String? {
        if (CurrentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken< List<Daily?>?>() {}.type
        return gson.toJson(CurrentValues, type)
    }

    @TypeConverter
    fun ToDailyList(CurrentString: String?): List<Daily?>? {
        if (CurrentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily?>>(CurrentString, type)
    }
    @TypeConverter
    fun fromHourlyList(CurrentValues: List<Hourly?>?): String? {
        if (CurrentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken< List<Hourly?>?>() {}.type
        return gson.toJson(CurrentValues, type)
    }

    @TypeConverter
    fun ToHourlyList(CurrentString: String?): List<Hourly?>? {
        if (CurrentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly?>?>() {}.type
        return gson.fromJson<List<Hourly?>>(CurrentString, type)
    }
    @TypeConverter
    fun fromAlertList(CurrentValues: List<Alert?>?): String? {
        if (CurrentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken< List<Alert?>?>() {}.type
        return gson.toJson(CurrentValues, type)
    }

    @TypeConverter
    fun ToAlertList(CurrentString: String?): List<Alert?>? {
        if (CurrentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Alert?>?>() {}.type
        return gson.fromJson<List<Alert?>>(CurrentString, type)
    }
}