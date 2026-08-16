package com.moviles.eventify.ui.fragments

import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.moviles.eventify.R

class SpeakersDetailDialogFragment : DialogFragment() {

    private var name: String? = null
    private var job: String? = null
    private var biography: String? = null
    private var twitter: String? = null
    private var image: String? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            performCall()
        } else {
            Toast.makeText(requireContext(), "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PHONE_NUMBER = "+51960301255"
        
        private const val ARG_NAME = "name"
        private const val ARG_JOB = "job"
        private const val ARG_BIOGRAPHY = "biography"
        private const val ARG_TWITTER = "twitter"
        private const val ARG_IMAGE = "image"

        @JvmStatic
        fun newInstance(name: String, job: String, biography: String, twitter: String, image: String) =
            SpeakersDetailDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_JOB, job)
                    putString(ARG_BIOGRAPHY, biography)
                    putString(ARG_TWITTER, twitter)
                    putString(ARG_IMAGE, image)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_NAME)
            job = it.getString(ARG_JOB)
            biography = it.getString(ARG_BIOGRAPHY)
            twitter = it.getString(ARG_TWITTER)
            image = it.getString(ARG_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_speakers_detail_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivSpeakerDetail: ImageView = view.findViewById(R.id.ivSpeakerDetail)
        val tvSpeakerNameDetail: TextView = view.findViewById(R.id.tvSpeakerNameDetail)
        val tvSpeakerJobDetail: TextView = view.findViewById(R.id.tvSpeakerJobDetail)
        val tvBiography: TextView = view.findViewById(R.id.tvBiography)
        val tvTwitter: TextView = view.findViewById(R.id.tvTwitter)
        val llSpeakerPhone: View = view.findViewById(R.id.llSpeakerPhone)
        val btnClose: View = view.findViewById(R.id.btnCloseSpeakerDialog)

        btnClose.setOnClickListener { dismiss() }

        tvSpeakerNameDetail.text = name
        tvSpeakerJobDetail.text = job
        tvBiography.text = biography
        tvTwitter.text = twitter

        tvTwitter.setOnClickListener {
            twitter?.let { handle ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = "https://twitter.com/$handle".toUri()
                }
                startActivity(intent)
            }
        }

        llSpeakerPhone.setOnClickListener {
            checkAndMakeCall()
        }

        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.ic_persona)
            .error(R.drawable.ic_persona)
            .circleCrop()
            .into(ivSpeakerDetail)
    }

    private fun checkAndMakeCall() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED -> {
                performCall()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    private fun performCall() {
        try {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = "tel:$PHONE_NUMBER".toUri()
            startActivity(callIntent)
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Error de seguridad al llamar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
