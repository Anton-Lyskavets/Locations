package com.example.locations.data

import kotlinx.coroutines.flow.Flow
import androidx.room.*
import com.example.locations.domain.model.Locations

@Dao
interface LocationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locations: Locations)

    @Query("SELECT * FROM locations")
    fun getLocations(): Flow<MutableList<Locations>>

    @Query("UPDATE locations SET nameLocation=:newNameLocation WHERE id = :id")
    suspend fun updateName(newNameLocation: String?, id: Int)

    @Query("UPDATE locations SET photosLocation=:newPhotosLocation WHERE id = :id")
    suspend fun updatePhotos(newPhotosLocation: MutableList<String>?, id: Int)

    @Query("DELETE FROM locations")
    suspend fun clearDataBase()
}