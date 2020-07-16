package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class ManejadorPermisosGPS(
    private val context: Context
)
{

    fun verificarPermisosGPS() : ManejadorPermisosGPS{
        if(!tengoHabilitado(Manifest.permission.ACCESS_FINE_LOCATION) && !tengoHabilitado(Manifest.permission.ACCESS_COARSE_LOCATION)){
            habilitarPermisos()
            return this
        }
        return this
    }

    private fun tengoHabilitado(permiso : String) : Boolean{
        return ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED
    }

    private fun habilitarPermisos(){

    }

}