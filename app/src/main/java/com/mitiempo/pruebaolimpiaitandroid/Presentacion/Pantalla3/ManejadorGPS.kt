package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mitiempo.pruebaolimpiaitandroid.R

class ManejadorGPS(
    private val context: Context
) {
    private var UPDATE_INTERVAL = 5000.toLong()
    private var FASTEST_INTERVAL = 5000.toLong()

    private inner class EscuchadorCallbackGoogleApiclient : GoogleApiClient.ConnectionCallbacks {
        override fun onConnected(p0: Bundle?) {
            inicializarActualizacionesDePosision()
        }

        private fun inicializarActualizacionesDePosision() {
            val locationRequest = LocationRequest()
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            locationRequest.setInterval(UPDATE_INTERVAL)
            locationRequest.setFastestInterval(FASTEST_INTERVAL)
            LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                locationRequest,
                escuchadorLocationListener
            )
        }

        override fun onConnectionSuspended(p0: Int) {}

    }

    private inner class EscuchadorFallaGoogleApi : GoogleApiClient.OnConnectionFailedListener {
        override fun onConnectionFailed(p0: ConnectionResult) {

        }

    }

    private var escuchadorRespuestas = EscuchadorCallbackGoogleApiclient()
    private var escuchadorFallaGoogleApi = EscuchadorFallaGoogleApi()


    private val googleApiClient = GoogleApiClient.Builder(context)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(escuchadorRespuestas)
        .addOnConnectionFailedListener(escuchadorFallaGoogleApi)
        .build()


    private var escuchadorCoordenadas: ((LatLng) -> Unit)? = null
    fun conEscuchadorCoordenadas(escuchadorCoordenadas: ((LatLng) -> Unit)): ManejadorGPS {
        this.escuchadorCoordenadas = escuchadorCoordenadas
        return this
    }

    private var escuchadorFalla: ((Int, Int) -> Unit)? = null
    fun conEscuchadorFalla(escuchadorFalla: ((Int, Int) -> Unit)): ManejadorGPS {
        this.escuchadorFalla = escuchadorFalla
        return this
    }

    fun onStart() :  ManejadorGPS{
        if (googleApiClient.isConnected) {
            return this
        }
        googleApiClient.connect()
        return this
    }

    fun onResume() : ManejadorGPS{
        if (seEstanEjecutandoLosServiciosDeposicion()) {
            return this
        }
        return this
    }

    private fun seEstanEjecutandoLosServiciosDeposicion(): Boolean {
        val apiDisponible = GoogleApiAvailability.getInstance()
        val resultCode = apiDisponible.isGooglePlayServicesAvailable(context)
        if (resultCode != ConnectionResult.SUCCESS) {
            escuchadorFalla?.invoke(R.string.GPS, R.string.surgio_un_error_en_gps)
            return false
        }
        return true
    }


    private var escuchadorLocationListener = EscuchadorLocationListener()
    fun onPause()  :  ManejadorGPS{
        if (!googleApiClient.isConnected) {
            return this
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(
            googleApiClient,
            escuchadorLocationListener
        )
        googleApiClient.disconnect()
        return this
    }

    private inner class EscuchadorLocationListener : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            if(p0 == null ){ return }
            Log.e("Error","Coordenadas ${p0}")
            escuchadorCoordenadas?.invoke(LatLng(p0.latitude,p0.longitude))
        }

    }

}