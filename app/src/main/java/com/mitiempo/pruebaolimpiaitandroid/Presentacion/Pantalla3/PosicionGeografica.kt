package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment
import com.mitiempo.pruebaolimpiaitandroid.R

class PosicionGeografica @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla3,this,true)
    }

    fun mostrarVista(){
        post {
            visibility = View.VISIBLE
            verificarPermisos()
            inicializarManejadorGoogleMaps()
        }
    }

    private var manejadorGPS : ManejadorPermisosGPS ?= null
    private fun verificarPermisos(){

        if(manejadorGPS == null ){
            manejadorGPS = ManejadorPermisosGPS(context)
        }

        manejadorGPS
            ?.conEscuchadorRespuestaNegativaDialogo {
                Log.e("Error","Tengo deshabilitado los permisos")
            }
            ?.conEscuchadorRespuestaPositivaDialogo {
                Log.e("Error","Tengo habilitado los permisos")

            }
            ?.verificarPermisos()
    }

    private var manejadorGoogleMap : ManejadorGoogleMap ?= null
    private var mapa : SupportMapFragment ?= null
    private fun inicializarManejadorGoogleMaps(){
        if(manejadorGoogleMap == null ){
            mapa = (context as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
            manejadorGoogleMap = ManejadorGoogleMap(context,mapa!!)
        }
        manejadorGoogleMap
            ?.ponerCoordenadas()
    }

    fun ocultarVista(){
        post {
            visibility = View.GONE
        }
    }

    fun conOnRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) : PosicionGeografica{
        manejadorGPS
            ?.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        return this
    }
}