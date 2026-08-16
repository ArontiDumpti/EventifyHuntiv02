package com.moviles.eventify.data.models

data class Event(
    val title: String = "",
    val description: String = "",
    val speaker: String = "",
    val tag: String = "",
    val datetime: Long = 0
)