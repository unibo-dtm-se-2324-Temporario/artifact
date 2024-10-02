package com.example.temporario.Events

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.time.LocalDateTime

class EventsRepository(
    var eventsDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Events")
) {
    //var eventsDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Events")

    fun getAllEventsFromDB (callback: (List<Event>) -> Unit) {
        val eventsList = mutableListOf<Event>()

        eventsDatabaseReference.addValueEventListener(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                eventsList.clear()
                for (eventSnapshot in snapshot.children) {
                    val key = eventSnapshot.key ?: ""
                    val userUID = eventSnapshot.child("userUID").value?.toString() ?: ""
                    val startTime = eventSnapshot.child("startTime").value?.toString() ?: ""
                    val duration = (eventSnapshot.child("duration").value as? Long)?.toInt() ?: 1
                    val description = eventSnapshot.child("description").value.toString()

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

    fun getEventsFromDB (uid: String, callback: (MutableList<Event>) -> Unit) {
        val eventsList = mutableListOf<Event>()

        eventsDatabaseReference.addValueEventListener(object: ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                eventsList.clear()
                for (eventSnapshot in snapshot.children) {

                    val userUID = eventSnapshot.child("userUID").value?.toString() ?: ""
                    if (userUID == uid) {
                        val key = eventSnapshot.key ?: ""
                        val startTime = eventSnapshot.child("startTime").value?.toString() ?: ""
                        val duration = (eventSnapshot.child("duration").value as? Long)?.toInt() ?: 1
                        val description = eventSnapshot.child("description").value.toString()

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
    fun getEventsByDate (events: List<Event>, day:Int, month: Int, year: Int,
                         callback: (MutableList<Event>) -> Unit) {
        val todayEvents = mutableListOf<Event>()
        for (event in events) {
            val startTime = event.startTime
            if (startTime!!.dayOfMonth == day && startTime.month.value == month &&
                        startTime.year == year) {
                todayEvents.add(event)
            }
        }
        callback(todayEvents)
    }
    fun addEventToDB (uid: String, description: String, startTime: LocalDateTime, duration: Int,
                      callback: (Int) -> Unit) {

        val newChildReference = eventsDatabaseReference.push()
        val key = newChildReference.key
        val event = Event(key!!, uid, description, startTime, duration)
        eventsDatabaseReference.child(key.toString()).setValue(event).addOnSuccessListener {
            callback(1)
        }.addOnFailureListener {
            callback(0)
        }
    }

    fun deleteEventFromDB (key: String, callback: (Int) -> Unit) {
        eventsDatabaseReference.child(key).removeValue().addOnSuccessListener {
            callback(1)
        }.addOnFailureListener {
//            callback(0)
        }
    }

    fun editEvent(events: MutableList<Event>, key: String, userUID: String, description: String,
                    date: LocalDateTime, duration: Int, callback: (MutableList<Event>) -> Unit) {
        val childReference = eventsDatabaseReference.child(key)

        childReference.updateChildren(
            mapOf(
                "description" to description,
                "duration" to duration,
                "startTime" to date
            )
        ).addOnSuccessListener {
            childReference.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val index = events.indexOfFirst { it.key == key }
                    val newEvent = Event(key, userUID, description, date, duration)
                    events[index] = newEvent
                    callback(events)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

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