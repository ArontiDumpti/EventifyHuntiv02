package com.moviles.eventify.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.moviles.eventify.R
import com.moviles.eventify.data.models.Speaker

class SpeakerAdapter(
    private var speakers: List<Speaker>,
    private val onSpeakerClick: (Speaker) -> Unit
) : RecyclerView.Adapter<SpeakerAdapter.SpeakerViewHolder>() {

    class SpeakerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivSpeakerImage: ShapeableImageView = view.findViewById(R.id.ivSpeakerImage)
        val tvSpeakerName: TextView = view.findViewById(R.id.tvSpeakerName)
        val tvSpeakerJobTitle: TextView = view.findViewById(R.id.tvSpeakerJobTitle)
        val tvSpeakerWorkplace: TextView = view.findViewById(R.id.tvSpeakerWorkplace)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_speaker, parent, false)
        return SpeakerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) {
        val speaker = speakers[position]
        holder.tvSpeakerName.text = speaker.name
        holder.tvSpeakerJobTitle.text = speaker.jobtitle
        holder.tvSpeakerWorkplace.text = speaker.workplace

        Glide.with(holder.itemView.context)
            .load(speaker.image)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.ivSpeakerImage)

        holder.itemView.setOnClickListener {
            onSpeakerClick(speaker)
        }
    }

    override fun getItemCount(): Int = speakers.size

    fun updateData(newSpeakers: List<Speaker>) {
        speakers = newSpeakers
        notifyDataSetChanged()
    }
}