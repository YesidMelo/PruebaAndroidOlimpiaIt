package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
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