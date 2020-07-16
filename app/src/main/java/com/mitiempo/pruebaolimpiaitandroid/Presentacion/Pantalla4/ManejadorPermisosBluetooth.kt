package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla4

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.CodigosActivityResult
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoDetallado
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoGenerico

class ManejadorPermisosBluetooth (
    private val context: Context
) {

    companion object{
        private var tengoPermisosHabilitados = false
    }

    private var escuchadorRespuestaPositivaDialogo : (()->Unit)?= null
    fun conEscuchadorRespuestaPositivaDialogo(escuchadorRespuestaPositivaDialogo : (()->Unit)) : ManejadorPermisosBluetooth{
        this.escuchadorRespuestaPositivaDialogo = escuchadorRespuestaPositivaDialogo
        return this
    }


    private var escuchadorRespuestaNegativaDialogo : (()->Unit)?= null
    fun conEscuchadorRespuestaNegativaDialogo(escuchadorRespuestaNegativaDialogo : (()->Unit)) : ManejadorPermisosBluetooth{
        this.escuchadorRespuestaNegativaDialogo = escuchadorRespuestaNegativaDialogo
        return this
    }

    fun verificarPermisos() : ManejadorPermisosBluetooth{
        habilitarPermisos()
        return this
    }

    private fun habilitarPermisos(){

        if(tengoPermisosHabilitados){
            escuchadorRespuestaPositivaDialogo?.invoke()
            return
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            tengoPermisosHabilitados = true
            escuchadorRespuestaPositivaDialogo?.invoke()
            return
        }

        solicitarPermisosParaVersionesPosterioresAM()

    }

    private fun solicitarPermisosParaVersionesPosterioresAM() {
        if(tengoHabilitado(Manifest.permission.ACCESS_FINE_LOCATION) && tengoHabilitado(Manifest.permission.ACCESS_COARSE_LOCATION)){
            tengoPermisosHabilitados = true
            escuchadorRespuestaPositivaDialogo?.invoke()
            return
        }

        solicitarPermisos()
    }

    private fun tengoHabilitado(permiso : String) : Boolean{
        return ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED
    }

    private fun solicitarPermisos(){
        context.mostrarDialogoDetallado(
            R.string.Bluetooth,
            R.string.permisos_bluetooth,
            DialogoGenerico.TipoDialogo.ADVERTENCIA,
            ::ejecutarSolicitantePermisosApp,
            { escuchadorRespuestaNegativaDialogo?.invoke()  },
            true
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun ejecutarSolicitantePermisosApp(){
        (context as Activity)
            .requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH
                ),
                CodigosActivityResult.BLUETOOTH.getPermissionResult()
            )
    }

    fun conOnRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) : ManejadorPermisosBluetooth{

        if (requestCode != CodigosActivityResult.BLUETOOTH.getPermissionResult()){ return this}

        if (tengoLosPermisosHabilitadosComoResultado(grantResults)){
            tengoPermisosHabilitados = true
            return this
        }

        mostrarAlertaPermisosDeshabilitados()

        return  this
    }

    private fun tengoLosPermisosHabilitadosComoResultado(grantResults : IntArray) : Boolean{
        return grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    private fun mostrarAlertaPermisosDeshabilitados(){
        context.mostrarDialogoGenerico()
    }


}