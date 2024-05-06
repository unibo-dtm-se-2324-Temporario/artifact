package com.example.temporario.Home

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.applandeo.materialcalendarview.utils.SelectedDay
import com.example.temporario.Events.EventsRepository
import com.example.temporario.databinding.FragmentCreateEventBinding
import java.time.LocalDateTime
import java.util.Calendar


class CreateEventFragment : Fragment() {

    private lateinit var binding: FragmentCreateEventBinding
    val repo = EventsRepository()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.showDatePicker.setOnClickListener {
            showDatePicker()
        }
        binding.createEvent.setOnClickListener {
            val description = binding.addDescription.text.toString()
        }
    }

    private fun showDatePicker() {
        Log.d("aoleu", "AOLEU")
        val listener: OnSelectDateListener = object : OnSelectDateListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSelect(calendar: List<Calendar>) {
                val selectedDay = calendar[0].get(Calendar.DAY_OF_MONTH)
                val selectedMonth = calendar[0].get(Calendar.MONTH) + 1
                val selectedYear = calendar[0].get(Calendar.YEAR)
                selectedDate.withDayOfMonth(selectedDay)
                selectedDate.withMonth(selectedMonth)
                selectedDate.withYear(selectedYear)
            }
        }

        val builder = DatePickerBuilder(requireContext(), listener)
            .pickerType(CalendarView.ONE_DAY_PICKER)
        val datePicker = builder.build()
        datePicker.show()
    }


}

