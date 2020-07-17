package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla4

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mitiempo.pruebaolimpiaitandroid.R

class ManejadorEstadosBluetooth (
    private val context: Context
) {

    private val adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter()

    private var escuchadorFalla : ((Int,Int)->Unit) ?= null
    fun conRespuestaFalla(escuchadorFalla : ((Int,Int)->Unit)) : ManejadorEstadosBluetooth{
        this.escuchadorFalla = escuchadorFalla
        return  this
    }

    enum class EstadosBluetooth(private val color : Int){
        APAGADO(R.color.rojo),
        ENCENDIENDO(R.color.verde_rojo),
        ENCENDIDO(R.color.verde),
        APAGANDO(R.color.rojo_verde),
        ;

        fun traerColor() : Int{
            return color
        }
    }


    private var escuchadorEstadoBluetooth : ((EstadosBluetooth)->Unit) ?=null
    fun conEscuchadorEstadosBluetooth(escuchadorEstadoBluetooth : ((EstadosBluetooth)->Unit)) : ManejadorEstadosBluetooth{
        this.escuchadorEstadoBluetooth = escuchadorEstadoBluetooth
        return this
    }

    fun encenderObservadorEstados() : ManejadorEstadosBluetooth{
        if(adaptadorBluetooth == null ){
            escuchadorFalla?.invoke(
                R.string.Bluetooth,
                R.string.este_dispositivo_no_tiene_bluetooth
            )
            return this
        }

        registrarBroadCast()

        return this
    }

    private fun registrarBroadCast(){
        try {
            val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            (context as AppCompatActivity).registerReceiver(broadCast,filter)
        }catch (e : Exception){
            (context as AppCompatActivity).unregisterReceiver(broadCast)
            registrarBroadCast()
        }
    }

    val broadCast = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

            if(intent == null ){ return }
            val accion = intent.action!!

            if(!accion.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){ return }

            val estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE , BluetoothAdapter.ERROR)

            when(estado){
                BluetoothAdapter.STATE_OFF -> {
                    escuchadorEstadoBluetooth?.invoke(EstadosBluetooth.APAGADO)
                }
                BluetoothAdapter.STATE_TURNING_ON ->{
                    escuchadorEstadoBluetooth?.invoke(EstadosBluetooth.ENCENDIENDO)
                }
                BluetoothAdapter.STATE_ON ->{
                    escuchadorEstadoBluetooth?.invoke(EstadosBluetooth.ENCENDIDO)
                }
                BluetoothAdapter.STATE_TURNING_OFF ->{
                    escuchadorEstadoBluetooth?.invoke(EstadosBluetooth.APAGANDO)
                }
            }

        }

    }


    fun apagarObservadorEstados() : ManejadorEstadosBluetooth{
        try {

            (context as AppCompatActivity).unregisterReceiver(broadCast)

        }catch (e : Exception){
            Log.e("Error","",e)
        }
        return this
    }

}