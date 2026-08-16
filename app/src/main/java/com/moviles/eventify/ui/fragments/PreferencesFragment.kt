package com.moviles.eventify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.moviles.eventify.data.preferences.PreferenceHelper
import com.moviles.eventify.databinding.FragmentPreferencesBinding
import com.moviles.eventify.network.ApiDniService

class PreferencesFragment : Fragment() {
    private var _binding: FragmentPreferencesBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceHelper = PreferenceHelper(requireContext())

        binding.btnSavePreferences.setOnClickListener {
            val name = binding.etUserName.text.toString().trim()
            val email = binding.etUserEmail.text.toString().trim()
            val notifications = binding.cbNotifications.isChecked

            if (name.isEmpty()) {
                binding.etUserName.error = "Ingrese su nombre de usuario"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.etUserEmail.error = "Ingrese su correo"
                return@setOnClickListener
            }

            preferenceHelper.saveUserName(name)
            preferenceHelper.saveUserEmail(email)
            preferenceHelper.saveNotificationsEnabled(notifications)
            preferenceHelper.saveLastSection("Preferencias")

            Toast.makeText(
                requireContext(),
                "Preferencias Guardadas correctamente",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnShowPreferences.setOnClickListener {
            val message = """
                Nombre: ${preferenceHelper.getUserName()}                
                Correo: ${preferenceHelper.getUserEmail()}           
                Notificaciones: ${if (preferenceHelper.areNotificacionsEnabled()) "Activadas" else "Desactivadas"}            
                Ultima Seccion: ${preferenceHelper.getLastSection()}
            """.trimIndent()
            binding.tvPreferencesResult.text = message
        }

        binding.btnClearPreferences.setOnClickListener {
            preferenceHelper.clearPreferences()

            binding.etUserName.setText("")
            binding.etUserEmail.setText("")
            binding.cbNotifications.isChecked = false
            binding.tvPreferencesResult.text = "Preferencias Eliminadas"

            Toast.makeText(
                requireContext(),
                "Preferencias Eliminadas",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnBuscardni.setOnClickListener {
            val dni = binding.etDNI.text.toString()
            if (dni.length != 8) {
                Toast.makeText(
                    requireContext(), "INGRESE DNI VALIDO", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            ApiDniService().buscarDni(dni) { respuesta ->
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(), respuesta, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
