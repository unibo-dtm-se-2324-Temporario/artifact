package com.example.temporario.Events

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.temporario.Home.EditEventFragment
import com.example.temporario.Home.HomeActivity
import com.example.temporario.R
import com.example.temporario.databinding.EventRowLayoutBinding

class CustomAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val onItemClick: (Event) -> Unit
): RecyclerView.Adapter<CustomAdapter.EventsHolder>() {

    lateinit var binding: EventRowLayoutBinding
    private val eventsList = mutableListOf<Event>()
    private val repo = EventsRepository()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsHolder {
        binding = EventRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsHolder(binding)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventsHolder, position: Int) {
        val item = eventsList[position]
        holder.description.text = item.description
        holder.startTime.text = "Start time: ".plus(item.startTime.toString())
        holder.duration.text = "Duration: ".plus(item.duration.toString()).plus("h")
        holder.deleteButton.setOnClickListener {
            repo.deleteEventFromDB(item.key.toString()) {
                if (it == 1) {
                    eventsList.removeAt(position)
                    notifyItemRemoved(position)
                    onItemClick(item)
                    Toast.makeText(context, "Event deleted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Oops. An error occurred!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.editButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("Key", item.key)
            bundle.putString("UID", item.userUID)
            bundle.putString("Description", item.description)
            bundle.putInt("Duration", item.duration ?: 1)
            bundle.putInt("Day", item.startTime!!.dayOfMonth)
            bundle.putInt("Month", item.startTime.monthValue)
            bundle.putInt("Year", item.startTime.year)
            bundle.putInt("Hour", item.startTime.hour)
            bundle.putInt("Minutes", item.startTime.minute)

            val fragment = EditEventFragment()
            fragment.arguments = bundle

            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_create, fragment, null)
                .commit()
            notifyDataSetChanged()
        }

    }

    class EventsHolder(binding: EventRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        var description = binding.eventDescription
        var startTime = binding.eventStartTime
        var duration = binding.eventDuration
        var deleteButton = binding.deleteEvent
        var editButton = binding.editEvent
        var constraint = binding.constraintLayout
    }

    fun update(list: List<Event>) {
        eventsList.clear()
        eventsList.addAll(list)
        notifyDataSetChanged()
    }



}