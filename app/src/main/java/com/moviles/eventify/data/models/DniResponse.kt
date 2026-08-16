package com.moviles.eventify.data.models

import com.google.gson.annotations.SerializedName

class DniResponse (
    val status: Int,
    val message: String,
    val success: Boolean,
    val data: PersonaDni
)
data class PersonaDni(
    val numero: String,
    val nombres: String,
    @SerializedName("apellido_paterno")
    val apellidoPaterno: String,
    @SerializedName("apellido_materno")
    val apellidoMaterno: String
)