package com.moviles.eventify.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.moviles.eventify.databinding.FragmentUbicationDetailDialogBinding

class UbicationDetailDialogFragment : DialogFragment() {

    private var _binding: FragmentUbicationDetailDialogBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            performCall()
        } else {
            performDial()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUbicationDetailDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCloseUbicationDialog.setOnClickListener { dismiss() }

        binding.cvUbicationPhone.setOnClickListener {
            checkAndMakeCall()
        }

        binding.cvUbicationWeb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = "https://www.uns.edu.pe".toUri()
            }
            startActivity(intent)
        }
    }

    private fun checkAndMakeCall() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            performCall()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    private fun performCall() {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = "tel:$PHONE_NUMBER".toUri()
            }
            startActivity(intent)
        } catch (e: SecurityException) {
            performDial()
        }
    }

    private fun performDial() {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$PHONE_NUMBER".toUri()
        }
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PHONE_NUMBER = "+51960301255"
    }
}
