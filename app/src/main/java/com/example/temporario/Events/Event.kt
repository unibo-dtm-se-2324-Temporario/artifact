package com.example.temporario.Events

import java.time.LocalDateTime
import java.util.Date

data class Event(
    val key: Int,
    val userUID: String,
    val description: String,
    val startTime: LocalDateTime?,
    val duration: Int?
    )
