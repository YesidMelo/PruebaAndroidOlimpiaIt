package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mitiempo.pruebaolimpiaitandroid.R

class ManejadorEstadosWifi( private val context: Context) {

    private var escuchadorFalla : ((Int,Int)->Unit)?= null
    fun conEscuchadorFalla(escuchadorFalla : ((Int,Int)->Unit)) : ManejadorEstadosWifi{
        this.escuchadorFalla = escuchadorFalla
        return this
    }

    enum class EstadosWifi(private val color : Int){
        DESHABILITADO(R.color.rojo),
        HABILITANDO(R.color.verde_rojo),
        HABILITADO(R.color.verde),
        DESHABILITANDO(R.color.rojo_verde),
        DESCONOCIDO(R.color.gris),
        ;

        fun traerColor() : Int{
            return color
        }
    }

    private var escuchadorEstadosWIFI : ((EstadosWifi)->Unit) ?= null
    fun conEscuchadorEstadosWifi(escuchadorEstadosWIFI : ((EstadosWifi)->Unit)) : ManejadorEstadosWifi{
        this.escuchadorEstadosWIFI = escuchadorEstadosWIFI
        return this
    }

    fun encenderObservadorEstadosWifi() : ManejadorEstadosWifi{
        registrarBroadcast()
        return this
    }

    private fun registrarBroadcast(){
        try {
            val filter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
            (context as AppCompatActivity).registerReceiver(broadcastReceiver,filter)
        }catch (e : Exception){
            (context as AppCompatActivity).unregisterReceiver(broadcastReceiver)
            registrarBroadcast()
        }
    }

    private val broadcastReceiver = object :BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent == null){ return }
            val estadosWifi = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN)

            when(estadosWifi){
                WifiManager.WIFI_STATE_DISABLED ->{
                    escuchadorEstadosWIFI?.invoke(EstadosWifi.DESHABILITADO)
                }

                WifiManager.WIFI_STATE_DISABLING ->{
                    escuchadorEstadosWIFI?.invoke(EstadosWifi.DESHABILITANDO)
                }

                WifiManager.WIFI_STATE_ENABLING ->{
                    escuchadorEstadosWIFI?.invoke(EstadosWifi.HABILITANDO)
                }

                WifiManager.WIFI_STATE_ENABLED ->{
                    escuchadorEstadosWIFI?.invoke(EstadosWifi.HABILITADO)
                }

                WifiManager.WIFI_STATE_UNKNOWN ->{
                    escuchadorEstadosWIFI?.invoke(EstadosWifi.DESCONOCIDO)
                }
            }

        }

    }

    fun apagarObservadorEstadosWifi() : ManejadorEstadosWifi{
        try {
            (context as AppCompatActivity).unregisterReceiver(broadcastReceiver)
        }catch (e : Exception){
            Log.e("Error","",e)
        }
        return this
    }

}