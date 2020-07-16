package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoDetallado

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
        }
    }

    private var manejadorGPS : ManejadorPermisosGPS ?= null
    private fun verificarPermisos(){

        if(manejadorGPS == null ){
            manejadorGPS = ManejadorPermisosGPS(context)
        }

        manejadorGPS
            ?.conEscuchadorRespuestaNegativaDialogo {
                context.mostrarDialogoDetallado(
                    R.string.GPS,
                    R.string.no_habilito_los_permisos_gps,
                    DialogoGenerico.TipoDialogo.ERROR
                )
            }
            ?.conEscuchadorRespuestaPositivaDialogo {
                Thread {
                    Thread.sleep(5_000)
                    inicializarManejadorGoogleMaps()
                }.start()

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
            ?.adicionarCoordenadas()
            ?.actualizarMapa()
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