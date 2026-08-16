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
import com.moviles.eventify.data.models.Speaker
import com.moviles.eventify.ui.adapters.SpeakerAdapter

class SpeakersFragment : Fragment() {
    private lateinit var rvSpeakers: RecyclerView
    private lateinit var speakerAdapter: SpeakerAdapter
    private lateinit var loadingOverlay: View
    private lateinit var fabAddSpeaker: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_speakers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvSpeakers = view.findViewById(R.id.rvSpeakers)
        loadingOverlay = view.findViewById(R.id.loadingOverlay)
        fabAddSpeaker = view.findViewById(R.id.fabAddSpeaker)

        speakerAdapter = SpeakerAdapter(emptyList()) { speaker ->
            val dialog = SpeakersDetailDialogFragment.newInstance(
                speaker.name,
                speaker.jobtitle,
                speaker.biography,
                speaker.twitter,
                speaker.image
            )
            dialog.show(parentFragmentManager, "SpeakersDetailDialog")
        }
        rvSpeakers.layoutManager = LinearLayoutManager(requireContext())
        rvSpeakers.adapter = speakerAdapter

        fabAddSpeaker.setOnClickListener {
            val addSpeakerDialog = AddSpeakerDialogFragment()
            addSpeakerDialog.show(childFragmentManager, "AddSpeakerDialog")
        }

        fetchSpeakers()
    }

    fun fetchSpeakers() {
        showLoading(true)
        val db = FirebaseFirestore.getInstance()
        db.collection("speakers")
            .get()
            .addOnSuccessListener { result ->
                val speakersList = result.toObjects(Speaker::class.java)
                speakerAdapter.updateData(speakersList)
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