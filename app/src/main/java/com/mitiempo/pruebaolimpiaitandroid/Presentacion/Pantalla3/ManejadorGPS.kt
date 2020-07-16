package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng

class ManejadorGPS(
    private val context: Context
) {

    private var escuchadorCoordenadas : ((LatLng)->Unit )?= null
    fun conEscuchadorCoordenadas(escuchadorCoordenadas : ((LatLng)->Unit )): ManejadorGPS{
        this.escuchadorCoordenadas = escuchadorCoordenadas
        return this
    }

    val locationManager = (context as AppCompatActivity).getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun inicializarVerificacion(){
        ponerEscuchaorALocationManager()
    }


    private val tiempoEspera = 5_000.toLong()
    private val otroValor = 10.toFloat()

    @SuppressLint("MissingPermission")
    private fun ponerEscuchaorALocationManager() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,tiempoEspera,otroValor,EscuchadorLocalizacion())
    }

    private inner class EscuchadorLocalizacion : LocationListener{
        override fun onLocationChanged(p0: Location?) {
            if(p0 == null ){ return }
            val localizacion = LatLng(p0.latitude,p0.latitude)
            escuchadorCoordenadas?.invoke(localizacion)
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

        }

        override fun onProviderEnabled(p0: String?) {

        }

        override fun onProviderDisabled(p0: String?) {

        }

    }

}