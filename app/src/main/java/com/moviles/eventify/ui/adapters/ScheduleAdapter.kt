package com.moviles.eventify.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moviles.eventify.R
import com.moviles.eventify.data.models.Event
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(
    private var events: List<Event>,
    private val onItemClick: (Event) -> Unit
) : RecyclerView.Adapter<ScheduleAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEventTitle: TextView = view.findViewById(R.id.tvEventTitle)
        val tvEventTag: TextView = view.findViewById(R.id.tvEventTag)
        val tvEventSpeaker: TextView = view.findViewById(R.id.tvEventSpeaker)
        val tvEventDate: TextView = view.findViewById(R.id.tvEventDate)
        val tvEventTime: TextView = view.findViewById(R.id.tvEventTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.tvEventTitle.text = event.title
        holder.tvEventTag.text = event.tag
        holder.tvEventSpeaker.text = event.speaker

        val dateSdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(event.datetime * 1000)
        
        holder.tvEventDate.text = dateSdf.format(date)
        holder.tvEventTime.text = timeSdf.format(date)

        holder.itemView.setOnClickListener {
            onItemClick(event)
        }
    }

    override fun getItemCount(): Int = events.size

    fun updateData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}