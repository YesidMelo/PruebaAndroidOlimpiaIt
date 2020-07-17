package com.mitiempo.pruebaolimpiaitandroid.Presentacion

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.mitiempo.pruebaolimpiaitandroid.Modelos.DetalleUsuario
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla1.FormularioRegistro
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla2.SeleccionarFoto
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3.PosicionGeografica
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla4.EstadosBluetoothWifi
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla5.GuardarInformacion

class ManejadorNavegacion(
    private val context: Context
) {

    private val usuario = DetalleUsuario()

    private var formularioRegistro : FormularioRegistro ?= null
    fun conFormularioRegistro(formularioRegistro: FormularioRegistro) : ManejadorNavegacion{
        this.formularioRegistro = formularioRegistro
        return this
    }

    private var posicionGeografica : PosicionGeografica ?= null
    fun conPosicionGeografica(posicionGeografica : PosicionGeografica) : ManejadorNavegacion{
        this.posicionGeografica = posicionGeografica
        return this
    }

    private var seleccionarFoto : SeleccionarFoto ?= null
    fun conSeleccionarFoto(seleccionarFoto : SeleccionarFoto) : ManejadorNavegacion{
        this.seleccionarFoto = seleccionarFoto
        return this
    }

    private var estadosBluetoothWifi : EstadosBluetoothWifi?= null
    fun conEstadosBluetoothWifi(estadosBluetoothWifi : EstadosBluetoothWifi) : ManejadorNavegacion{
        this.estadosBluetoothWifi = estadosBluetoothWifi
        return this
    }

    private var guardar_informacion : GuardarInformacion ?= null
    fun conGuardarInformacion(guardar_informacion : GuardarInformacion) : ManejadorNavegacion{
        this.guardar_informacion = guardar_informacion
        return this
    }

    private fun ejecutarEnHiloPrincipal (funcion : ()->Unit){
        (context as AppCompatActivity).runOnUiThread {
            funcion.invoke()
        }
    }

    private fun mostrarFormularioRegistro(){
        formularioRegistro?.mostrarVista()
        posicionGeografica?.ocultarVista()
        seleccionarFoto?.ocultarVista()
        estadosBluetoothWifi?.ocultarVista()
        guardar_informacion?.ocultarVista()
    }

    private fun mostrarPosicionGeografica(){
        formularioRegistro?.ocultarVista()
        posicionGeografica?.mostrarVista()
        seleccionarFoto?.ocultarVista()
        estadosBluetoothWifi?.ocultarVista()
        guardar_informacion?.ocultarVista()
    }

    private fun mostrarSeleccionarFoto(){
        formularioRegistro?.ocultarVista()
        posicionGeografica?.ocultarVista()
        seleccionarFoto?.mostrarVista()
        estadosBluetoothWifi?.ocultarVista()
        guardar_informacion?.ocultarVista()
    }

    private fun mostrarEstadosBluetoothWifi(){
        formularioRegistro?.ocultarVista()
        posicionGeografica?.ocultarVista()
        seleccionarFoto?.ocultarVista()
        estadosBluetoothWifi?.mostrarVista()
        guardar_informacion?.ocultarVista()
    }

    private fun mostrarGuardarInformacion(){
        formularioRegistro?.ocultarVista()
        posicionGeografica?.ocultarVista()
        seleccionarFoto?.ocultarVista()
        estadosBluetoothWifi?.ocultarVista()
        guardar_informacion?.mostrarVista()
    }

    private enum class ListaVistas (){
        FORMULARIO_REGISTRO,
        POSICION_GEOGRAFICA,
        SELECCIONAR_FOTO,
        ESTADOS_BLUETOOTH_WIFI,
        GUARDAR_INFORMACION,

        ;
        private var mostrarVista : (()->Unit) ?= null
        fun conFuncionMostrarVista(mostrarVista : (()->Unit)) : ListaVistas{
            this.mostrarVista = mostrarVista
            return this
        }

        fun traerMostrarVista() : (()->Unit)?{
            return mostrarVista
        }
    }

    fun inicializarNavegacion(){
        adicionaUsuarioALasVistas()
        adicionaFuncionesAlEnumeradorListaVistas()

    }

    private fun adicionaUsuarioALasVistas(){

    }

    private fun adicionaFuncionesAlEnumeradorListaVistas(){

        ListaVistas.FORMULARIO_REGISTRO.conFuncionMostrarVista(::mostrarFormularioRegistro)
        ListaVistas.POSICION_GEOGRAFICA.conFuncionMostrarVista(::mostrarPosicionGeografica)
        ListaVistas.SELECCIONAR_FOTO.conFuncionMostrarVista(::mostrarSeleccionarFoto)
        ListaVistas.ESTADOS_BLUETOOTH_WIFI.conFuncionMostrarVista(::mostrarEstadosBluetoothWifi)
        ListaVistas.GUARDAR_INFORMACION.conFuncionMostrarVista(::mostrarGuardarInformacion)

    }

}