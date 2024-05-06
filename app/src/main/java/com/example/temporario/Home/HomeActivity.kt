package com.example.temporario.Home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.example.temporario.Event
import com.example.temporario.EventsRepository
import com.example.temporario.R
import com.example.temporario.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Calendar

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var calendarView: com.applandeo.materialcalendarview.CalendarView
    private val calendarDays: MutableList<CalendarDay> = ArrayList()
    var eventsList: List<Event> = ArrayList()
    val repo = EventsRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userUID = intent.getStringExtra("FirebaseUserUID")
        calendarView = binding.calendar
        renderEvents(userUID!!)
        setEventListeners()
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
                    eventDay.set(Calendar.MONTH, month)
                }
                if (day != null) {
                    eventDay.set(Calendar.DAY_OF_MONTH, day)
                    Toast.makeText(this, "$month", Toast.LENGTH_SHORT).show()
                }
                val caleDay = CalendarDay(eventDay)
                caleDay.imageResource = R.drawable.ic_purple_circle_24
                calendarDays.add(caleDay)
                //Toast.makeText(this, "${calendarDays.size}", Toast.LENGTH_LONG).show()
            }

            //Log.d("Calendar days", "$calendarDays")
            //Toast.makeText(this, "${calendarDays.size}", Toast.LENGTH_LONG).show()

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

                val fragment = PopUpFragment()
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, null)
                    .commit()
            }
        })

    }



}