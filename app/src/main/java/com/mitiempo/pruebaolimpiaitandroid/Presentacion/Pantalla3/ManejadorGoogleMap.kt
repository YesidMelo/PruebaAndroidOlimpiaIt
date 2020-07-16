package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        ejecutarEnHiloPrincipal{ supportMapFragment .getMapAsync(manejadorGoogleMap()) }
    }

    private fun ejecutarEnHiloPrincipal( funcion : () ->Unit){
        (context as AppCompatActivity).runOnUiThread{
            funcion.invoke()
        }
    }

    private var googleMap : GoogleMap ?= null
    private inner class manejadorGoogleMap : OnMapReadyCallback{
        override fun onMapReady(p0: GoogleMap?) {
            googleMap = p0
        }
    }

    private val listaCoordenadas = emptyMap<String,LatLng>().toMutableMap()
    fun adicionarCoordenadas(latLng: LatLng = LatLng(-34.0,151.0), titulo : String = "Sidney") : ManejadorGoogleMap{
        listaCoordenadas[titulo] = latLng


        return this
    }

    fun actualizarMapa(){
        Thread{
            try {
                while (googleMap == null ){
                    Thread.sleep(500)
                }
                adicionarMarcadores()
            }catch (e : Exception){
                Log.e("Error","",e)
            }
        }.start()
    }

    private fun adicionarMarcadores(){
        for (detalle in listaCoordenadas.entries){
            ejecutarEnHiloPrincipal {
                Log.e("Error","Coordenadas : ${detalle}");
                googleMap?.addMarker(MarkerOptions().position(detalle.value).title(detalle.key))
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(detalle.value))
            }
        }
    }





}