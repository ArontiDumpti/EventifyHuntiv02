package com.moviles.eventify.ui.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.moviles.eventify.R
import com.moviles.eventify.databinding.FragmentUbicationBinding

class UbicationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentUbicationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUbicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Bottom card is visible by default in this professional concept
        binding.cvMapDetail.setOnClickListener {
            val dialog = UbicationDetailDialogFragment()
            dialog.show(parentFragmentManager, "UbicationDetailDialog")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Location for Universidad Nacional del Santa
        val centerLocation = LatLng(-9.1195283, -78.5158914) 
        
        // 1. I created a custom vector drawable (ic_custom_marker) that mimics 
        // the professional concept: a purple circular pin with a white star inside.
        val customMarkerIcon = bitmapDescriptorFromVector(requireContext(), R.drawable.ic_custom_marker)

        // 2. Added the main event marker with the new professional style
        googleMap.addMarker(
            MarkerOptions()
                .position(centerLocation)
                .title("Universidad Nacional del Santa")
                .icon(customMarkerIcon)
        )

        // 3. Added decorative markers to achieve the high-density look from the concept image
        val locations = listOf(
            LatLng(-9.1215, -78.5140) to "Mercado Bellamar",
            LatLng(-9.1180, -78.5175) to "SENATI",
            LatLng(-9.1230, -78.5120) to "Centro Chimbote"
        )

        locations.forEach { (latLng, title) ->
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .icon(customMarkerIcon)
            )
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerLocation, 15.5f))

        googleMap.setOnMarkerClickListener { marker ->
            // Update bottom card details based on marker
            binding.tvMapTitle.text = marker.title
            
            // Show detail dialog
            val dialog = UbicationDetailDialogFragment()
            dialog.show(parentFragmentManager, "UbicationDetailDialog")
            true
        }

        // Apply dark mode map style
        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.custom_map))
        } catch (e: Exception) {
            // Fallback if resource is missing
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
