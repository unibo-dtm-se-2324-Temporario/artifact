package com.example.temporario.Home

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.temporario.databinding.FragmentCreateEventBinding
import java.time.LocalDateTime
import java.util.Calendar
import kotlin.properties.Delegates


class CreateEventFragment : Fragment() {

    private lateinit var binding: FragmentCreateEventBinding
    val repo = EventsRepository()
    private lateinit var userUID: String
    private var duration = 1

    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate: LocalDateTime = LocalDateTime.now()

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

        binding.title.text = "Create a new event!"
        binding.addDescription.hint = "Description"
        binding.createEvent.text = "Create"

        userUID = arguments?.getString("UID")!!

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
            val description = binding.addDescription.text.toString()
            repo.addEventToDB(userUID, description, selectedDate, duration) {
                if (it == 0) {
                    Toast.makeText(requireContext(), "Oops. An error occurred!", Toast.LENGTH_SHORT).show()
                } else if (it == 1) {
                    Toast.makeText(requireContext(), "Event created!", Toast.LENGTH_SHORT).show()
                }
                closeFragment()
            }

        }

        binding.exitButton.setOnClickListener {
            closeFragment()
        }
    }

    private fun closeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    private fun showDatePicker() {
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
            }, 12, 12, true)

        timePickerDialog.show()
    }


}

