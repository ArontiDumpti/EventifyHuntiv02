package com.moviles.eventify.ui.fragments

import android.app.DatePickerDialog
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
import com.moviles.eventify.data.models.Event
import java.util.Calendar

class AddEventDialogFragment : DialogFragment() {

    private var selectedDateTime: Long = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTitle = view.findViewById<TextInputEditText>(R.id.etEventTitle)
        val etDescription = view.findViewById<TextInputEditText>(R.id.etEventDescription)
        val etSpeaker = view.findViewById<TextInputEditText>(R.id.etEventSpeaker)
        val etTag = view.findViewById<TextInputEditText>(R.id.etEventTag)
        val etDate = view.findViewById<TextInputEditText>(R.id.etEventDate)
        val btnSave = view.findViewById<Button>(R.id.btnSaveEvent)

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                    selectedDateTime = selectedCalendar.timeInMillis
                    etDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()
            val speaker = etSpeaker.text.toString()
            val tag = etTag.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val event = Event(
                    title = title,
                    description = description,
                    speaker = speaker,
                    tag = tag,
                    datetime = selectedDateTime
                )
                saveEventToFirestore(event)
            } else {
                Toast.makeText(requireContext(), "Please fill in title and description", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveEventToFirestore(event: Event) {
        val db = FirebaseFirestore.getInstance()
        db.collection("events")
            .add(event)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Event saved successfully", Toast.LENGTH_SHORT).show()
                dismiss()
                (parentFragment as? ScheduleFragment)?.fetchSchedule()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error saving event: ${e.message}", Toast.LENGTH_SHORT).show()
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