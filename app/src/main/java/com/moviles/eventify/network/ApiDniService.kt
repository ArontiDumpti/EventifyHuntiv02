package com.moviles.eventify.network

import com.google.gson.Gson
import com.moviles.eventify.data.models.DniResponse
import okhttp3.*
import okhttp3.Callback
import java.io.IOException

class ApiDniService {

    private val client = OkHttpClient()
    private val gson = Gson()

    private val token = "YOUR_API_TOKEN_HERE" // REMOVED HARDCODED TOKEN FOR SECURITY
    fun buscarDni(
        dni: String,
        callback: (String) -> Unit
    ) {
        val dniLimpio = dni.trim()

        if (dniLimpio.length != 8 || !dniLimpio.all { it.isDigit() }) {
            callback("El DNI debe tener exactamente 8 números")
            return
        }

        val request = Request.Builder()
            .url("https://api.factiliza.com/v1/dni/info/$dniLimpio")
            .addHeader(
                "Authorization",
                "Bearer $token"
            )
            .addHeader(
                "Accept",
                "application/json"
            )
            .get()
            .build()

        client.newCall(request).enqueue(
            object : Callback {

                override fun onFailure(
                    call: Call,
                    e: IOException
                ) {
                    callback(
                        "Error de conexión: ${e.message}"
                    )
                }

                override fun onResponse(
                    call: Call,
                    response: Response
                ) {
                    response.use { respuesta ->

                        val body = respuesta.body?.string()

                        if (!respuesta.isSuccessful) {
                            callback(
                                "Error HTTP ${respuesta.code}"
                            )
                            return
                        }

                        if (body.isNullOrBlank()) {
                            callback("Sin respuesta")
                            return
                        }

                        try {
                            val persona = gson.fromJson(
                                body,
                                DniResponse::class.java
                            )

                            val datos = persona.data

                            if (datos == null) {
                                callback(
                                    persona.message
                                        ?: "No se encontraron datos"
                                )
                                return
                            }

                            val nombreCompleto = listOfNotNull(
                                datos.nombres,
                                datos.apellidoPaterno,
                                datos.apellidoMaterno
                            )
                                .map { it.trim() }
                                .filter { it.isNotBlank() }
                                .joinToString(" ")

                            if (nombreCompleto.isBlank()) {
                                callback(
                                    "No se obtuvo el nombre completo"
                                )
                            } else {
                                callback(nombreCompleto)
                            }

                        } catch (e: Exception) {
                            callback(
                                "Error al procesar la respuesta: ${e.message}"
                            )
                        }
                    }
                }
            }
        )
    }
}