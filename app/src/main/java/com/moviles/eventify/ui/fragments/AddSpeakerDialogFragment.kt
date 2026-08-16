package com.moviles.eventify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.moviles.eventify.R
import com.moviles.eventify.data.models.Speaker

class AddSpeakerDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_speaker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<TextInputEditText>(R.id.etSpeakerName)
        val etJobTitle = view.findViewById<TextInputEditText>(R.id.etSpeakerJobTitle)
        val etWorkplace = view.findViewById<TextInputEditText>(R.id.etSpeakerWorkplace)
        val etBiography = view.findViewById<TextInputEditText>(R.id.etSpeakerBiography)
        val etTwitter = view.findViewById<TextInputEditText>(R.id.etSpeakerTwitter)
        val etImage = view.findViewById<TextInputEditText>(R.id.etSpeakerImage)
        val etCategory = view.findViewById<TextInputEditText>(R.id.etSpeakerCategory)
        val btnSave = view.findViewById<Button>(R.id.btnSaveSpeaker)

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val jobtitle = etJobTitle.text.toString()
            val workplace = etWorkplace.text.toString()
            val biography = etBiography.text.toString()
            val twitter = etTwitter.text.toString()
            val image = etImage.text.toString()
            val categoryStr = etCategory.text.toString()

            if (name.isNotEmpty() && jobtitle.isNotEmpty()) {
                val category = categoryStr.toIntOrNull() ?: 0
                val speaker = Speaker(
                    name = name,
                    jobtitle = jobtitle,
                    workplace = workplace,
                    biography = biography,
                    twitter = twitter,
                    image = image,
                    category = category
                )
                saveSpeakerToFirestore(speaker)
            } else {
                Toast.makeText(requireContext(), "Please fill in Name and Job Title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveSpeakerToFirestore(speaker: Speaker) {
        val db = FirebaseFirestore.getInstance()
        db.collection("speakers")
            .add(speaker)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Speaker saved successfully", Toast.LENGTH_SHORT).show()
                dismiss()
                (parentFragment as? SpeakersFragment)?.fetchSpeakers()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error saving speaker: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}