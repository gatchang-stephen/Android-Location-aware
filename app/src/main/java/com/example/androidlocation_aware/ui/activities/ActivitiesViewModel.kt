package com.example.androidlocation_aware.ui.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidlocation_aware.data.OutdoorRepository
import com.example.androidlocation_aware.data.OutdoorRoomDatabase
import com.example.androidlocation_aware.data.OutdoorRoomRepository

class ActivitiesViewModel(application: Application) : AndroidViewModel(application) {
    private val outdoorRepository: OutdoorRepository

    init {
        val outdoorDao = OutdoorRoomDatabase.getInstance(application).outdoorDao()
        outdoorRepository = OutdoorRoomRepository(outdoorDao)
    }

    val allActivities = outdoorRepository.getAllActivities()

    fun toggleGeofencing(id: Int) = outdoorRepository.toggleActivityGeofence(id)
}