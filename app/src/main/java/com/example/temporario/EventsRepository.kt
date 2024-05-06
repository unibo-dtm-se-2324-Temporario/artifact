package com.example.temporario

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.time.LocalDateTime

class EventsRepository {

    private val database = Firebase.database.reference

    fun getAllEventsFromDB (callback: (List<Event>) -> Unit) {
        val eventsList = mutableListOf<Event>()

        database.child("Events").addValueEventListener(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                eventsList.clear()
                for (eventSnapshot in snapshot.children) {
                    val key = eventSnapshot.key?.toInt() ?: -1
                    val userUID = eventSnapshot.child("userUID").value?.toString() ?: ""
                    val startTime = eventSnapshot.child("startTime").value?.toString() ?: ""
                    val duration = (eventSnapshot.child("duration").value as? Long)?.toInt() ?: 1
                    val description = eventSnapshot.child("description").value.toString() ?: ""

                    var startDate: LocalDateTime? = null
                    if (startTime != "") {
                        startDate = getTime(startTime)
                    }

                    val event = Event(key, userUID, description, startDate, duration)
                    eventsList.add(event)
                }
                callback(eventsList)
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }

    fun getEventsFromDB (uid: String, callback: (List<Event>) -> Unit) {
        val eventsList = mutableListOf<Event>()

        database.child("Events").addValueEventListener(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                eventsList.clear()
                for (eventSnapshot in snapshot.children) {

                    val userUID = eventSnapshot.child("userUID").value?.toString() ?: ""
                    if (userUID == uid) {
                        val key = eventSnapshot.key?.toInt() ?: -1

                        val startTime = eventSnapshot.child("startTime").value?.toString() ?: ""
                        val duration = (eventSnapshot.child("duration").value as? Long)?.toInt() ?: 1
                        val description = eventSnapshot.child("description").value.toString() ?: ""

                        var startDate: LocalDateTime? = null
                        if (startTime != "") {
                            startDate = getTime(startTime)
                        }

                        val event = Event(key, userUID, description, startDate, duration)
                        eventsList.add(event)
                    }
                }
                callback(eventsList)
            }
            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEventsByDate (events: List<Event>, day:Int, month: Int, year: Int, callback: (List<Event>) -> Unit) {
        val todayEvents = mutableListOf<Event>()
        for (event in events) {
            val startTime = event.startTime
//            Log.d("Start time", "${startTime!!.dayOfMonth} ${startTime!!.month.value +1} ${startTime!!.year} ")
//            Log.d("Ziua de azi", "$day $month $year")
            if (startTime!!.dayOfMonth == day && startTime.month.value + 1 == month &&
                        startTime.year == year) {
                todayEvents.add(event)
            }
        }

        callback(todayEvents)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTime(str: String): LocalDateTime {
        val jsonObj = JSONObject(str)
        val day = jsonObj.getInt("dayOfMonth")
        val month = jsonObj.getInt("monthValue")
        val year = jsonObj.getInt("year")
        val hours = jsonObj.getInt("hour")
        val minutes = jsonObj.getInt("minute")

        return LocalDateTime.of(year, month, day, hours, minutes)
    }
}