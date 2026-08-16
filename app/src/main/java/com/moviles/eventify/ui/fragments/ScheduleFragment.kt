package com.moviles.eventify.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.moviles.eventify.R
import com.moviles.eventify.data.models.Event
import com.moviles.eventify.ui.adapters.ScheduleAdapter

class ScheduleFragment : Fragment() {
    private lateinit var rvSchedule: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var loadingOverlay: View
    private lateinit var fabAddEvent: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvSchedule = view.findViewById(R.id.rvSchedule)
        loadingOverlay = view.findViewById(R.id.loadingOverlaySchedule)
        fabAddEvent = view.findViewById(R.id.fabAddEvent)

        scheduleAdapter = ScheduleAdapter(emptyList()) { event ->
            val dialog = ScheduleDetailDialogFragment.newInstance(
                event.title,
                event.description,
                event.speaker,
                event.tag,
                event.datetime
            )
            dialog.show(parentFragmentManager, "ScheduleDetailDialog")
        }

        rvSchedule.layoutManager = LinearLayoutManager(requireContext())
        rvSchedule.adapter = scheduleAdapter

        fabAddEvent.setOnClickListener {
            val addEventDialog = AddEventDialogFragment()
            addEventDialog.show(childFragmentManager, "AddEventDialog")
        }

        fetchSchedule()
    }

    fun fetchSchedule() {
        showLoading(true)
        val db = FirebaseFirestore.getInstance()
        db.collection("events")
            .get()
            .addOnSuccessListener { result ->
                val eventsList = result.toObjects(Event::class.java)
                scheduleAdapter.updateData(eventsList)
                showLoading(false)
            }
            .addOnFailureListener {
                showLoading(false)
            }
    }

    private fun showLoading(isLoading: Boolean) {
        loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}