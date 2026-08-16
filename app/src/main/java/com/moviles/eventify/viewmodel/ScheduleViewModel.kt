package com.moviles.eventify.viewmodel

import com.moviles.eventify.network.FirestoreService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviles.eventify.data.models.Event

class ScheduleViewModel : ViewModel() {
    val firestoreService = FirestoreService()
    val listSchedule: MutableLiveData<List<Event>> = MutableLiveData()
}
