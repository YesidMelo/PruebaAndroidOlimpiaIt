package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla4

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.mitiempo.pruebaolimpiaitandroid.Modelos.DetalleUsuario
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoDetallado
import kotlinx.android.synthetic.main.pantalla4.view.*

class EstadosBluetoothWifi @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)
{

    private var usuario : DetalleUsuario?= null
    fun conUsuario(usuario : DetalleUsuario) : EstadosBluetoothWifi{
        this.usuario = usuario
        return this
    }

    private var escuchadorSiguiente : (()->Unit) ?= null
    fun conEscuchadorSiguiente(escuchadorSiguiente : (()->Unit)) : EstadosBluetoothWifi{
        this.escuchadorSiguiente = escuchadorSiguiente
        return this
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla4,this,true)
        ponerEscuchadores()
    }

    private fun ponerEscuchadores() {
        boton_siguiente_estados_bluetooth.setOnClickListener {
            escuchadorSiguiente?.invoke()
        }
    }

    fun mostrarVista(){
        post {
            inicializarManejadorPermisosBluetooth()
            inicializarManejadorEstadosBluetooth()
            inicializarManejadorEstadosWifi()
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
                actualizarColorEstadosBluetooth(estadoBluetooth)
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

    private fun actualizarColorEstadosBluetooth(estadoBluetooth: ManejadorEstadosBluetooth.EstadosBluetooth) {
        post {
            usuario?.estadosBluetooth = estadoBluetooth
            color_estado_bluetooth.setColorFilter(context.resources.getColor(estadoBluetooth.traerColor()))
        }
    }

    private var manejadorEstadosWifi : ManejadorEstadosWifi ?= null
    private fun inicializarManejadorEstadosWifi(){

        if(manejadorEstadosWifi != null){ return }
        manejadorEstadosWifi = ManejadorEstadosWifi(context)
            .conEscuchadorFalla { titulo, mensaje ->
                context.mostrarDialogoDetallado(
                    titulo,
                    mensaje,
                    DialogoGenerico.TipoDialogo.ERROR
                )
            }
            .conEscuchadorEstadosWifi {
                estadoWifi ->
                actualizarColorEstadosWifi(estadoWifi)
            }
            .encenderObservadorEstadosWifi()
    }

    private fun actualizarColorEstadosWifi(estadoWifi: ManejadorEstadosWifi.EstadosWifi) {
        post {
            usuario?.estadosWifi = estadoWifi
            color_estado_wifi.setColorFilter(context.resources.getColor(estadoWifi.traerColor()))
        }
    }

    fun onDestroy(){
        manejadorEstadosBluetooth?.apagarObservadorEstados()
        manejadorEstadosWifi?.apagarObservadorEstadosWifi()
    }

    fun conOnRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) : EstadosBluetoothWifi{
        manejadorPermisosBluetooth?.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        return this
    }

    fun ocultarVista(){
        post {
            visibility = View.GONE
        }
    }



}