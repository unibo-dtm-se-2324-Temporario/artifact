package com.example.temporario.Home

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarDay
import com.example.temporario.EventsRepository
import com.example.temporario.R
import com.example.temporario.databinding.ActivityHomeBinding
import java.util.Calendar

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var calendar: com.applandeo.materialcalendarview.CalendarView
    private lateinit var dateView: TextView
    private val eventsMap = mutableMapOf<String, List<String>>()
    private val calendarDays: MutableList<CalendarDay> = ArrayList()
    val repo = EventsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getStringExtra("FirebaseUser")

        calendar = binding.calendar

        repo.getAllEventsFromDB { events ->
            for (event in events) {
                val year = event.startTime?.year
                val month = event.startTime?.month
                val day = event.startTime?.day

                val eventDay = Calendar.getInstance()
                if (year != null) {
                    eventDay.set(Calendar.YEAR, year)
                }
                if (month != null) {
                    eventDay.set(Calendar.MONTH, month)
                }
                if (day != null) {
                    eventDay.set(Calendar.DAY_OF_MONTH, day)
                }
                val caleDay = CalendarDay(eventDay)
                caleDay.imageResource = R.drawable.ic_purple_circle_24
                calendarDays.add(caleDay)
            }
        }

        val calendarView: com.applandeo.materialcalendarview.CalendarView = calendar
        calendarView.setCalendarDays(calendarDays)

    }


}