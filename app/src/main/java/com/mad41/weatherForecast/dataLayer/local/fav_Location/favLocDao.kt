package com.mad41.weatherForecast.dataLayer.local.fav_Location

import androidx.room.*
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
@Dao
interface favLocDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favLocInsert(favLoc: favLocation)

    @Query("SELECT * FROM favLocations")
    fun getFavLocations(): List<favLocation>

    @Query("DELETE FROM favLocations WHERE address = :address")
    suspend fun deleteLocation(address :String)

   /* @Delete
    suspend fun deleteLocation(favLoc: favLocation)*/
}