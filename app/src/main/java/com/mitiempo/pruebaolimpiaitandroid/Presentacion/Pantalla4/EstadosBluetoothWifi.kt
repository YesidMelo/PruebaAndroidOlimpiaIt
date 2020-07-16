package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla4

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.mitiempo.pruebaolimpiaitandroid.R

class EstadosBluetoothWifi @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)
{

    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla4,this,true)

    }

    fun mostrarVista(){
        post {
            inicializarManejadorBluetooth()
            visibility = View.VISIBLE
        }
    }

    private var manejadorPermisosBluetooth : ManejadorPermisosBluetooth  ?= null

    private fun inicializarManejadorBluetooth(){
        if (manejadorPermisosBluetooth != null ){ return }
        manejadorPermisosBluetooth = ManejadorPermisosBluetooth(context)
            .conEscuchadorRespuestaPositivaDialogo {

            }
            .conEscuchadorRespuestaNegativaDialogo {

            }
            .verificarPermisos()
    }

    fun conOnRequestPermissionsResultconOnRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) : EstadosBluetoothWifi{
        manejadorPermisosBluetooth?.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        return this
    }

    fun ocultarVista(){
        post {
            visibility = View.GONE
        }
    }



}