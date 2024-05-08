package com.example.temporario.Events

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.temporario.Home.HomeActivity
import com.example.temporario.databinding.EventRowLayoutBinding

class CustomAdapter(
    private val context: Context,
    private val onItemClick: (Event) -> Unit
): RecyclerView.Adapter<CustomAdapter.EventsHolder>() {

    lateinit var binding: EventRowLayoutBinding
    private val eventsList = mutableListOf<Event>()
    val repo = EventsRepository()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsHolder {
        binding = EventRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsHolder(binding)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

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
                    Toast.makeText(context, "Event deleted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Oops. An error occurred!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    class EventsHolder(binding: EventRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        var description = binding.eventDescription
        var startTime = binding.eventStartTime
        var duration = binding.eventDuration
        var deleteButton = binding.deleteEvent
    }

    fun update(list: List<Event>) {
        eventsList.clear()
        eventsList.addAll(list)
        notifyDataSetChanged()
    }
}