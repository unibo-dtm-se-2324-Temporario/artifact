package com.example.temporario.Home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.example.temporario.Events.Event
import com.example.temporario.Events.EventsRepository
import com.example.temporario.Login.MainActivity
import com.example.temporario.R
import com.example.temporario.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.util.Calendar

class HomeActivity: AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var calendarView: com.applandeo.materialcalendarview.CalendarView
    private val calendarDays: MutableList<CalendarDay> = ArrayList()
    var eventsList: MutableList<Event> = ArrayList()
    private val repo = EventsRepository()
    lateinit var userUID: String
    private val auth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userUID = intent.getStringExtra("FirebaseUserUID")!!
        calendarView = binding.calendar
        getEvents(userUID)
        setEventListeners()

        binding.addEvent.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("UID", userUID)
            val fragment = CreateEventFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_create, fragment, null)
                .commit()
        }

        binding.signoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEvents(uid: String) {
        repo.getEventsFromDB(uid) { events ->
            eventsList = events
            renderEvents()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun editEvent(key:String, userUID: String, description: String, date: LocalDateTime, duration:Int,
                  lastDay: Int, lastMonth: Int, lastYear:Int) {
        repo.editEvent(eventsList, key, userUID, description, date, duration) { events ->
            eventsList = events
            checkDay(lastDay, lastMonth, lastYear)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDay(day: Int, month: Int, year: Int) {
        var todayEvents: MutableList<Event> = ArrayList()

        repo.getEventsByDate(eventsList, day, month, year) {
            todayEvents = it
        }

        if (todayEvents.isEmpty()) {
            calendarDays.removeAll { it.calendar.get(Calendar.DAY_OF_MONTH) == day
                    && it.calendar.get(Calendar.MONTH) == month - 1
                    && it.calendar.get(Calendar.YEAR) == year}
            calendarView.setCalendarDays(calendarDays)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun renderEvents() {
        for (event in eventsList) {
            val year = event.startTime?.year
            val month = event.startTime?.monthValue
            val day = event.startTime?.dayOfMonth

            val eventDay = Calendar.getInstance()
            if (year != null) {
                eventDay.set(Calendar.YEAR, year)
            }
            if (month != null) {
                eventDay.set(Calendar.MONTH, month - 1)
            }
            if (day != null) {
                eventDay.set(Calendar.DAY_OF_MONTH, day)
            }
            val caleDay = CalendarDay(eventDay)
            caleDay.imageResource = R.drawable.ic_purple_circle_24
            calendarDays.add(caleDay)
        }
        calendarView.setCalendarDays(calendarDays)
    }

    private fun setEventListeners() {
        calendarView.setOnCalendarDayClickListener(object: OnCalendarDayClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(calendarDay: CalendarDay) {
                val clickedDay: Calendar = calendarDay.calendar
                val selectedDay = clickedDay.get(Calendar.DAY_OF_MONTH)
                val selectedMonth = clickedDay.get(Calendar.MONTH) + 1
                val selectedYear = clickedDay.get(Calendar.YEAR)

                val bundle = Bundle()
                bundle.putInt("Day", selectedDay)
                bundle.putInt("Month", selectedMonth)
                bundle.putInt("Year", selectedYear)

                val fragment = ListEventsFragment()
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_create, fragment, null)
                    .commit()

            }
        })

    }



}