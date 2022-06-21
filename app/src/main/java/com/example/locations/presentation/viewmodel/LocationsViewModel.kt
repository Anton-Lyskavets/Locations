package com.example.locations.presentation.viewmodel

import androidx.lifecycle.*
import com.example.locations.domain.model.Locations
import com.example.locations.data.LocationsDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LocationsViewModel(private val locationsDao: LocationsDao) : ViewModel() {
    val allLocations: LiveData<MutableList<Locations>> = locationsDao.getLocations().asLiveData()
    fun insertLocation(locations: Locations) {
        viewModelScope.launch {
            locationsDao.insert(locations)
        }
    }

    private fun getNewLocationEntry(
        nameLocation: String,
        photosLocation: MutableList<String>,
    ): Locations {
        return Locations(
            nameLocation = nameLocation,
            photosLocation = photosLocation
        )
    }

    fun addNewLocation(nameLocation: String, photosLocation: MutableList<String>) {
        val newLocation = getNewLocationEntry(nameLocation, photosLocation)
        insertLocation(newLocation)
    }

    private fun insertNewPhotos(locations: Locations) {
        viewModelScope.launch {
            locationsDao.updatePhotos(locations.photosLocation, locations.id)
        }
    }

    fun updatePhotos(locations: Locations) {
        val updateLocation = locations.copy(
            id = locations.id,
            nameLocation = locations.nameLocation,
            photosLocation = locations.photosLocation)
        insertNewPhotos(updateLocation)
    }

    private fun insertNewName(locations: Locations) {
        viewModelScope.launch {
            locationsDao.updateName(locations.nameLocation, locations.id)
        }
    }

    fun updateName(locations: Locations) {
        val updateLocation = locations.copy(
            id = locations.id,
            nameLocation = locations.nameLocation,
            photosLocation = locations.photosLocation)
        insertNewName(updateLocation)
    }

    fun clearDB() {
        viewModelScope.launch {
            locationsDao.clearDataBase()
        }
    }
}

class LocationsViewModelFactory(private val locationsDao: LocationsDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationsViewModel(locationsDao) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }

}

