package com.example.temporario.Home

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.example.temporario.databinding.FragmentCreateEventBinding
import java.time.LocalDateTime
import java.util.Calendar

class EditEventFragment : Fragment() {

    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var description: String
    var duration = 0
    private lateinit var key: String
    private lateinit var year: Number
    private lateinit var month: Number
    private lateinit var day: Number
    private lateinit var userUID: String
    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate: LocalDateTime = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString("Key").toString()
            userUID = it.getString("UID").toString()
            description = it.getString("Description").toString()
            duration = it.getInt("Duration")
            day = it.getInt("Day")
            month = it.getInt("Month")
            year = it.getInt("Year")
            val hour = it.getInt("Hour")
            val minutes = it.getInt("Minutes")
            selectedDate = selectedDate.withDayOfMonth(day as Int)
                .withMonth(month as Int).withYear(year as Int).withHour(hour).withMinute(minutes)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // avoid action when clicking on the background
        binding.fragmentCreateBackground.setOnTouchListener { _, _ ->
            true
        }

        binding.title.text = "Edit your event"
        binding.addDescription.hint = description
        binding.seekBar.progress = duration
        binding.createEvent.text = "Save event"

        binding.showDatePicker.setOnClickListener {
            showDatePicker()
        }
        binding.showTimePicker.setOnClickListener {
            showTimePicker()
        }
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                duration = p1
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.createEvent.setOnClickListener {

            if (binding.addDescription.text.toString() != "") {
                description = binding.addDescription.text.toString()
            }

            (activity as HomeActivity).editEvent(key, userUID, description, selectedDate, duration,
                day as Int, month as Int, year as Int)
            Toast.makeText(requireContext(), "Event saved", Toast.LENGTH_SHORT).show()
            closeFragment()
        }

        binding.exitButton.setOnClickListener {
            closeFragment()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.set(selectedDate.year, selectedDate.month.value - 1, selectedDate.dayOfMonth)

        val listener: OnSelectDateListener = object : OnSelectDateListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSelect(calendar: List<Calendar>) {
                val selectedDay = calendar[0].get(Calendar.DAY_OF_MONTH)
                val selectedMonth = calendar[0].get(Calendar.MONTH) + 1
                val selectedYear = calendar[0].get(Calendar.YEAR)
                selectedDate = selectedDate.withDayOfMonth(selectedDay)
                    .withMonth(selectedMonth)
                    .withYear(selectedYear)
            }
        }

        val builder = DatePickerBuilder(requireContext(), listener)
            .pickerType(CalendarView.ONE_DAY_PICKER)
            .date(calendar)
        val datePicker = builder.build()
        datePicker.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(requireContext(),
            {
                    _, i, i2 ->
                selectedDate = selectedDate.withHour(i)
                    .withMinute(i2)
            }, selectedDate.hour, selectedDate.minute, true)

        timePickerDialog.show()
    }

    private fun closeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

}