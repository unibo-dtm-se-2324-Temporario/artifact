package com.example.temporario.Home

import com.example.temporario.Event
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.temporario.databinding.EventRowLayoutBinding

class CustomAdapter(
    private val context: Context,
    private val onItemClick: (Event) -> Unit
): RecyclerView.Adapter<CustomAdapter.EventsHolder>() {

    lateinit var binding: EventRowLayoutBinding
    private val eventsList = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsHolder {
        binding = EventRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsHolder(binding)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: EventsHolder, position: Int) {
        val item = eventsList[position]
        Log.d("inside bind", "Hi")
        holder.description.text = item.description
        Log.d("Holder description", holder.description.text.toString())
        holder.startTime.text = "Start time: ".plus(item.startTime.toString())
        holder.duration.text = "Duration: ".plus(item.duration.toString()).plus("h")
    }

    class EventsHolder(binding: EventRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        var description = binding.eventDescription
        var startTime = binding.eventStartTime
        var duration = binding.eventDuration
    }

    fun update(list: List<Event>) {
        eventsList.clear()
        eventsList.addAll(list)
        notifyDataSetChanged()
    }
}