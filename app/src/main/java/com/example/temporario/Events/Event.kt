package com.example.temporario.Events

import java.time.LocalDateTime

data class Event(
    val key: String,
    val userUID: String,
    val description: String,
    val startTime: LocalDateTime?,
    val duration: Int?
    )
