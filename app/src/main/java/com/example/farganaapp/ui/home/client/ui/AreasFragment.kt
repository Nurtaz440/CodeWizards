package com.example.farganaapp.ui.home.client.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.farganaapp.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AreasFragment  : Fragment()  {
    private var mGoogleMap : GoogleMap? = null
    lateinit var mAdView : AdView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_areas, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            (childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment).also {

                it.getMapAsync(OnMapReadyCallback { googleMap ->
                    // When map is loaded
                    googleMap.setOnMapClickListener { latLng -> // When clicked on map
                        // Initialize marker options
                        val markerOptions = MarkerOptions()
                        // Set position of marker
                        markerOptions.position(latLng)
                        // Set title of marker
                        markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)
                        // Remove all marker
                        googleMap.clear()
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                        // Add marker on map
                        googleMap.addMarker(markerOptions)
                    }
                })
            }


        MobileAds.initialize(requireContext()) {}

        mAdView = view.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//
//        mGoogleMap = googleMap
//        val sydney = LatLng(-34.0,151.0)
//        mGoogleMap!!.addMarker(MarkerOptions().position(sydney))
//        mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }
}