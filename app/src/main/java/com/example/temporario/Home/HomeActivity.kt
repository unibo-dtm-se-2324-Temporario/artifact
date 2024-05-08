package com.example.temporario.Home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.example.temporario.Events.Event
import com.example.temporario.Events.EventsRepository
import com.example.temporario.R
import com.example.temporario.databinding.ActivityHomeBinding
import java.util.Calendar

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var calendarView: com.applandeo.materialcalendarview.CalendarView
    private val calendarDays: MutableList<CalendarDay> = ArrayList()
    var eventsList: List<Event> = ArrayList()
    val repo = EventsRepository()
    private lateinit var userUID: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userUID = intent.getStringExtra("FirebaseUserUID")!!
        calendarView = binding.calendar
        renderEvents(userUID!!)
        setEventListeners()

        binding.addEvent.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("UID", userUID)
            val fragment = CreateEventFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_create, fragment, null)
                .commit()
            renderEvents(userUID)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderEvents(uid: String) {
        repo.getEventsFromDB(uid) { events ->
            eventsList = events
            for (event in events) {
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
    }

    private fun setEventListeners() {
        calendarView.setOnCalendarDayClickListener(object: OnCalendarDayClickListener {
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