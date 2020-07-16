package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3

import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ManejadorGoogleMap(
    private var context: Context,
    private var supportMapFragment : SupportMapFragment
) {



    init {
        configurarSupportMapFragment()
    }

    private fun configurarSupportMapFragment(){
        supportMapFragment
            .getMapAsync(manejadorGoogleMap())
    }

    private var googleMap : GoogleMap ?= null
    private inner class manejadorGoogleMap : OnMapReadyCallback{
        override fun onMapReady(p0: GoogleMap?) {
            googleMap = p0
        }
    }


    fun ponerCoordenadas(latLng: LatLng = LatLng(-34.0,151.0),titulo : String = "Sidney"){
        googleMap?.addMarker(MarkerOptions().position(latLng).title(titulo))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

}