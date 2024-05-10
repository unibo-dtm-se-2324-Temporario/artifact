package com.example.temporario.Home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.temporario.Events.CustomAdapter
import com.example.temporario.Events.Event
import com.example.temporario.Events.EventsRepository
import com.example.temporario.databinding.FragmentListEventsBinding

class ListEventsFragment : Fragment() {

    private lateinit var binding: FragmentListEventsBinding
    private lateinit var customAdapter: CustomAdapter
    private val repo = EventsRepository()
    private var eventsOfUser: List<Event>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDay = arguments?.getInt("Day")
        val currentMonth = arguments?.getInt("Month")
        val currentYear = arguments?.getInt("Year")

        customAdapter = CustomAdapter(requireContext(), requireActivity().supportFragmentManager) {item ->
            (activity as? HomeActivity)!!.checkDay(item.startTime!!.dayOfMonth,
                item.startTime.monthValue, item.startTime.year)
        }
        binding.eventsList.adapter = customAdapter

        eventsOfUser = (activity as? HomeActivity)?.eventsList

        if (!eventsOfUser?.isEmpty()!!) {
            repo.getEventsByDate(eventsOfUser!!, currentDay!!, currentMonth!!, currentYear!!) { list ->
                customAdapter.update(list)
                if (list.isEmpty()) {

                    binding.noEventsText.visibility = View.VISIBLE
                    (activity as HomeActivity).getEvents((activity as HomeActivity).userUID)
                }
            }
        }

        binding.exitButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }

    }


}