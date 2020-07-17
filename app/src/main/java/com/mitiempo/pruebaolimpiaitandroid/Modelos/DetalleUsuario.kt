package com.mitiempo.pruebaolimpiaitandroid.Modelos

import com.google.gson.annotations.Expose
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla4.ManejadorEstadosBluetooth
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla4.ManejadorEstadosWifi

class DetalleUsuario : ModeloBase {

    var nombre : String ?= null
    var cedula  : String ?= null
    var direccion : String ?= null
    var ciudad : String ?= null
    var pais : String ?= null
    var celular : String ?= null
    var foto : String ?= null
    var Bluetooth : String ?= null
    var Wifi : String ?= null
    var GPS : GPS ?= null

    @Expose(serialize = false)
    var estadosWifi : ManejadorEstadosWifi.EstadosWifi = ManejadorEstadosWifi.EstadosWifi.DESHABILITADO
    set(value) {
        field = value
        when(value){
            ManejadorEstadosWifi.EstadosWifi.HABILITADO ->{
                Bluetooth = "Habilitado"
            }
            else ->{
                Bluetooth = "Deshabilitado"
            }
        }
    }

    @Expose(serialize = false)
    var estadosBluetooth : ManejadorEstadosBluetooth.EstadosBluetooth = ManejadorEstadosBluetooth.EstadosBluetooth.APAGADO
    set(value) {
        field = value
        when(value){
            ManejadorEstadosBluetooth.EstadosBluetooth.ENCENDIDO -> {
                Wifi = "Encendido"
            }
            else ->{
                Wifi = "Apagado"
            }
        }
    }

}
