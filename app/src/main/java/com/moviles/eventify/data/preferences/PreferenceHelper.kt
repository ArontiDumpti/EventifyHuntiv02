package com.moviles.eventify.data.preferences

import android.content.Context

class PreferenceHelper(context: Context){
        private val preferences= context.getSharedPreferences(
            "eventify_preferences", Context.MODE_PRIVATE
        )
    fun saveUserName(name: String){
        preferences.edit()
            .putString("user_name",name)
            .apply()
    }

    fun getUserName(): String{
        return preferences.getString("user_name","Invitado") ?: "Invitado"
    }

    fun saveUserEmail(email: String){
        preferences.edit()
            .putString("user_email",email)
            .apply()
    }

    fun getUserEmail(): String{
        return preferences.getString("user_email","Sin correo registrado") ?:"Sin correo registrado"
    }

    fun saveNotificationsEnabled(enabled: Boolean){
        preferences.edit()
            .putBoolean("notifications_enabled",enabled)
            .apply()
    }

    fun areNotificacionsEnabled(): Boolean{
        return preferences.getBoolean("Notifications_enabled", false)
    }

    fun saveLastSection(section: String){
        preferences.edit()
            .putString("last_section",section)
            .apply()
    }

    fun getLastSection(): String {
            return preferences.getString("last_section","Inicio") ?:"Inicio"
    }

    fun clearPreferences(){
            preferences.edit()
                .clear()
                .apply()
    }
}