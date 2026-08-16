package com.moviles.eventify.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.moviles.eventify.data.models.Event
import com.moviles.eventify.data.models.Speaker



class FirestoreService {
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    init {
        firebaseFirestore.firestoreSettings = settings
    }

    fun getSpeakers(callback: Callback<List<Speaker>>) {

        firebaseFirestore.collection("speakers")
            .orderBy("category")
            .get()
            .addOnSuccessListener { result ->

                for (doc in result) {

                    val list = result.toObjects(Speaker::class.java)

                    callback.onSuccess(list)

                    break
                }
            }
    }



    fun getEvent(){



    }





}