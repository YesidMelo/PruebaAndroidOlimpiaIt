package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla2.camara

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.escuchap.escucha.Presentation.UtilidadesVista.SelectorImagen.camara.ManejadorPermisosCamara
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.CodigosActivityResult
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.crearDocumentoImagen
import java.io.File
import java.lang.ClassCastException

class BotonCamara  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){

    companion object{
        private var insancia : BotonCamara ?= null

        fun getInstancia(): BotonCamara?{
            return insancia
        }
    }

    private val T = "BotonCamara"

    init {
        insancia = this
        LayoutInflater.from(context).inflate(R.layout.boton_camara,this,true)
        conEscuchadores()
    }



    private fun conEscuchadores(){
        setOnClickListener {
            post {
                escuchadorClickBoton?.invoke()
                verificarPermisosCamara()
            }
        }
    }

    private var mostreActivity = false
    private fun verificarPermisosCamara(){
        ManejadorPermisosCamara
            .getInstancia()
            .conContexto(if(context is AppCompatActivity) context else appCompatActivity!!)
            .conEscuchadorRespuestaPositivaDialogo {
                if(mostreActivity){ return@conEscuchadorRespuestaPositivaDialogo}
                crearFoto()
            }
            .conEscuhadorRespuestaNegativaDialogo {  }
            .verificarPermisos()
    }


    private fun crearFoto(){
        mostreActivity = true
        crearDocumento()
        llamarActividadCamara()
    }
    private var documentoImagen : File? = null
    private fun crearDocumento(){
        try {
            documentoImagen = context.crearDocumentoImagen()
        }catch (e:Exception){
            Log.e(T,"",e)
        }
    }


    private fun llamarActividadCamara(){

        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (callCameraIntent.resolveActivity(this.context.packageManager) == null) return

        val authorities = this.context.applicationContext.packageName + ".fileprovider"
        val imageUri = FileProvider.getUriForFile(this.context, authorities, documentoImagen!!.absoluteFile)

        callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        try {
            (this.context as AppCompatActivity).startActivityForResult(callCameraIntent, CodigosActivityResult.CAMARA.getCodigoActivity())
        } catch (e: ClassCastException) {
            appCompatActivity?.startActivityForResult(callCameraIntent, CodigosActivityResult.CAMARA.getCodigoActivity())
        } catch (e :Exception){
            e.printStackTrace()
        }

    }

    private var escuchadorClickBoton :(()->Unit)?= null
    fun conEscuchadorClick(escuchadorClickBoton :(()->Unit)) : BotonCamara{
        this.escuchadorClickBoton = escuchadorClickBoton
        return this
    }


    private var escuchadorImagenCargada : ((Bitmap?)->Unit)?= null
    fun conEscuchadorImagenCargada(escuchadorImagenCargada : ((Bitmap?)->Unit)) :  BotonCamara{
        this.escuchadorImagenCargada = escuchadorImagenCargada
        return this
    }

    private var appCompatActivity : AppCompatActivity ?= null
    fun conActivity (appCompatActivity : AppCompatActivity): BotonCamara{
        this.appCompatActivity = appCompatActivity
        return this
    }

    fun conOnRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) : BotonCamara{
        ManejadorPermisosCamara
            .getInstancia()
            .conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        return this
    }

    fun escuchaOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) : BotonCamara
    {
        if (requestCode != CodigosActivityResult.CAMARA.getCodigoActivity()){ return this }
        if(documentoImagen == null ){ return this}
        RotadorImagenCamara()
            .conContext(context)
            .conRutaImagenCamara(documentoImagen!!.absolutePath)
            .conEscuchadorImagenRotada {
                escuchadorImagenCargada?.invoke(it)
            }
            .rotarImagen()

        mostreActivity = false
        return this
    }



}