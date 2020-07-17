package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla4

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoDetallado
import kotlinx.android.synthetic.main.pantalla4.view.*

class EstadosBluetoothWifi @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)
{

    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla4,this,true)

    }

    fun mostrarVista(){
        post {
            inicializarManejadorPermisosBluetooth()
            inicializarManejadorEstadosBluetooth()
            visibility = View.VISIBLE
        }
    }

    private var manejadorPermisosBluetooth : ManejadorPermisosBluetooth  ?= null

    private fun inicializarManejadorPermisosBluetooth(){
        if (manejadorPermisosBluetooth != null ){ return }
        manejadorPermisosBluetooth = ManejadorPermisosBluetooth(context)
            .conEscuchadorRespuestaPositivaDialogo {

            }
            .conEscuchadorRespuestaNegativaDialogo {

            }
            .verificarPermisos()
    }

    private var manejadorEstadosBluetooth : ManejadorEstadosBluetooth ?= null
    private fun inicializarManejadorEstadosBluetooth(){
        if(manejadorEstadosBluetooth != null ){ return }

        manejadorEstadosBluetooth = ManejadorEstadosBluetooth(context)
            .conEscuchadorEstadosBluetooth {
                estadoBluetooth ->
                actualizarColorEstados(estadoBluetooth)
            }
            .conRespuestaFalla { titulo, mensaje ->
                context.mostrarDialogoDetallado(
                    titulo,
                    mensaje,
                    DialogoGenerico.TipoDialogo.ERROR
                )
            }
            .encenderObservadorEstados()
    }

    private fun actualizarColorEstados(estadoBluetooth: ManejadorEstadosBluetooth.EstadosBluetooth) {
        post {
            color_estado_bluetooth.setColorFilter(context.resources.getColor(estadoBluetooth.traerColor()))
        }
    }

    fun onDestroy(){
        manejadorEstadosBluetooth?.apagarObservadorEstados()
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