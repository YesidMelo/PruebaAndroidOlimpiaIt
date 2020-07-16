package com.escuchap.escucha.Presentation.UtilidadesVista.SelectorImagen.camara

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.CodigosActivityResult
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoDetallado
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoGenerico

class ManejadorPermisosCamara private constructor(){


    companion object{
        private var tengoLosPermisosHabilitados = false
        private var instancia : ManejadorPermisosCamara ?= null

        fun getInstancia() : ManejadorPermisosCamara{
            if(instancia == null ){
                instancia = ManejadorPermisosCamara()
            }
            return instancia!!
        }
    }



    private var context : Context?= null
    fun conContexto(context: Context) :  ManejadorPermisosCamara{
        this.context = context
        return this
    }





    class ManejadorPermisosSinContexto : Exception("ManejadorPermisos No tiene un contexto")
    fun verificarPermisos() : ManejadorPermisosCamara{
        if(context == null ){
            throw ManejadorPermisosSinContexto()
        }
        if(tengoLosPermisosHabilitados){
            escuchadorRespuestaPositivaDialogo?.invoke()
            return this
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            escuchadorRespuestaPositivaDialogo?.invoke()
            tengoLosPermisosHabilitados = true
            return this
        }

        solicitarPermisosVersionesPosterioresM()

        return this
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun solicitarPermisosVersionesPosterioresM(){
        if(tengoLosPermisosEnEstadoGranted()){
            escuchadorRespuestaPositivaDialogo?.invoke()
            return
        }

        solicitarPermisos()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun tengoLosPermisosEnEstadoGranted() : Boolean{
        return context?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun solicitarPermisos(){

        if(context == null ){
            throw ManejadorPermisosSinContexto()
        }
        context!!.mostrarDialogoDetallado(
            R.string.camera_option,
            R.string.camara_deshabilitada,
            DialogoGenerico.TipoDialogo.ADVERTENCIA,
            ::ejecutarSolicitanteDePermisosEnApp,
            {escuchadorRespuestaNegativaDialogo?.invoke()},
            true
        )

    }




    @RequiresApi(Build.VERSION_CODES.M)
    private fun ejecutarSolicitanteDePermisosEnApp(){
        (context as? Activity)
            ?.requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CodigosActivityResult.CAMARA.getPermissionResult()
            )
    }


    fun conOnRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) : ManejadorPermisosCamara{
        if (requestCode != CodigosActivityResult.CAMARA.getPermissionResult()){
            return this
        }

        if(tengoLosPermisosHabilitadosComoResultdo(grantResults)){
            escuchadorRespuestaPositivaDialogo?.invoke()
            return this
        }

        mostrarAlertaPermisosDeshabilitados()
        return this
    }

    private fun tengoLosPermisosHabilitadosComoResultdo(grantResults: IntArray) : Boolean{
        return  grantResults[0] == PackageManager.PERMISSION_GRANTED
    }


    private fun mostrarAlertaPermisosDeshabilitados(){
        if(context == null ){
            throw ManejadorPermisosSinContexto()
        }

        context?.mostrarDialogoGenerico()
    }


    private var escuchadorRespuestaNegativaDialogo : (()->Unit)?= null
    fun conEscuhadorRespuestaNegativaDialogo(accionRespuestaNegativaDialogo : (()->Unit)) : ManejadorPermisosCamara{
        this.escuchadorRespuestaNegativaDialogo = accionRespuestaNegativaDialogo
        return this
    }

    private var escuchadorRespuestaPositivaDialogo : (()->Unit)?= null
    fun conEscuchadorRespuestaPositivaDialogo(escuchadorRespuestaPositivaDialogo : (()->Unit)) : ManejadorPermisosCamara{
        this.escuchadorRespuestaPositivaDialogo = escuchadorRespuestaPositivaDialogo
        return this
    }

}