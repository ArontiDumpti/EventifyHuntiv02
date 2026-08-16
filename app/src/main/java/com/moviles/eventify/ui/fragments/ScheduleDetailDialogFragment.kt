package com.moviles.eventify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.moviles.eventify.R
import java.text.SimpleDateFormat
import java.util.*

class ScheduleDetailDialogFragment : DialogFragment() {

    private var title: String? = null
    private var description: String? = null
    private var speaker: String? = null
    private var tag: String? = null
    private var datetime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("title")
            description = it.getString("description")
            speaker = it.getString("speaker")
            tag = it.getString("tag")
            datetime = it.getLong("datetime")
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule_detail_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnCloseDialog).setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btnBackSchedule).setOnClickListener { dismiss() }

        val tvDetailTitle: TextView = view.findViewById(R.id.tvDetailTitle)
        val tvDetailDate: TextView = view.findViewById(R.id.tvDetailDate)
        val tvDetailSpeakerName: TextView = view.findViewById(R.id.tvDetailSpeakerName)
        val tvDetailSpeakerTag: TextView = view.findViewById(R.id.tvDetailSpeakerTag)
        val tvDetailDescription: TextView = view.findViewById(R.id.tvDetailDescription)

        tvDetailTitle.text = title
        tvDetailSpeakerName.text = speaker
        tvDetailSpeakerTag.text = tag
        tvDetailDescription.text = description

        val sdf = SimpleDateFormat("EEEE d 'de' MMMM, HH:mm", Locale.forLanguageTag("es"))
        val date = Date(datetime * 1000)
        tvDetailDate.text = sdf.format(date).replaceFirstChar { it.uppercase() }
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, description: String, speaker: String, tag: String, datetime: Long) =
            ScheduleDetailDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putString("description", description)
                    putString("speaker", speaker)
                    putString("tag", tag)
                    putLong("datetime", datetime)
                }
            }
    }
}