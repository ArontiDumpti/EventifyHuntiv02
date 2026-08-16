package com.moviles.eventify.network

interface Callback<T>{
    fun onSuccess(result: T)
    fun onFailed(exception: Exception)
}