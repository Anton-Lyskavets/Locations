package com.example.locations.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.locations.domain.converter.Converter
import com.example.locations.domain.model.Locations

@Database(entities = [Locations::class], version = 3, exportSchema = false)
@TypeConverters(Converter::class)
abstract class LocationsRoomDatabase : RoomDatabase() {
    abstract fun locationsDao(): LocationsDao

    companion object {
        @Volatile
        private var INSTANCE: LocationsRoomDatabase? = null
        fun getDatabase(context: Context): LocationsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationsRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}