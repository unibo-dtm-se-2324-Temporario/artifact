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
import com.example.temporario.Events.EventsRepository
import com.example.temporario.R
import com.example.temporario.databinding.FragmentCreateEventBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

class EditEventFragment : Fragment() {

    private lateinit var binding: FragmentCreateEventBinding
    lateinit var description: String
    var duration = 0
    var key = 0
    lateinit var userUID: String
    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate: LocalDateTime = LocalDateTime.now()
    val repo = EventsRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getInt("Key")
            userUID = it.getString("UID").toString()
            description = it.getString("Description").toString()
            duration = it.getInt("Duration")
            val day = it.getInt("Day")
            val month = it.getInt("Month")
            val year = it.getInt("Year")
            val hour = it.getInt("Hour")
            val minutes = it.getInt("Minutes")
            selectedDate = selectedDate.withDayOfMonth(day)
                .withMonth(month).withYear(year).withHour(hour).withMinute(minutes)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = "Modify your event"
        binding.addDescription.hint = description
        binding.seekBar.progress = duration
        binding.createEvent.text = "Modify event"

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

            repo.modifyEvent(key, userUID, description, selectedDate, duration) {}
            Toast.makeText(requireContext(), "Event modified", Toast.LENGTH_SHORT).show()
            closeFragment()
        }

        binding.exitButton.setOnClickListener {
            closeFragment()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.set(selectedDate.year, selectedDate.month.value, selectedDate.dayOfMonth)

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
            .setDate(calendar)
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