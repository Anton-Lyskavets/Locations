package com.example.locations.presentation.app

import android.app.Application
import com.example.locations.data.LocationsRoomDatabase

class LocationsApplication : Application() {
    val database: LocationsRoomDatabase by lazy {
        LocationsRoomDatabase.getDatabase(this)
    }
}