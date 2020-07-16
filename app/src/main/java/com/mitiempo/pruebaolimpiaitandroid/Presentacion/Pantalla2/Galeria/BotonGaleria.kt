package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla2.Galeria

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.escuchap.escucha.Presentation.UtilidadesVista.SelectorImagen.Galeria.ManejadorPermisosGaleria
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.CodigosActivityResult

class BotonGaleria @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    init {
        LayoutInflater.from(context).inflate(R.layout.boton_galeria,this,true)
        conEscuchadores()
    }

    private fun conEscuchadores(){
        setOnClickListener {
            escuchadorClick?.invoke()
            verificarPermisos()
        }
    }

    private var mostreActivity = false
    private fun verificarPermisos(){

        ManejadorPermisosGaleria
            .getInstancia()
            .conEscuchadorRespuestaPositivaDialogo {
                if(mostreActivity){ return@conEscuchadorRespuestaPositivaDialogo}
                mostrarGaleria()
            }
            .conEscuhadorRespuestaNegativaDialogo {

            }
            .conContexto(if(context is AppCompatActivity) context else appCompatActivity!!)
            .verificarPermisos()
    }



    private fun mostrarGaleria(){
        mostreActivity = true
        val fotoIntent = Intent(Intent.ACTION_PICK)
        fotoIntent.setType("image/*")
        try {
            (this@BotonGaleria.context as Activity).startActivityForResult(fotoIntent, CodigosActivityResult.GALERIA.getCodigoActivity())
        } catch (e: ClassCastException) {
            appCompatActivity?.startActivityForResult(fotoIntent,CodigosActivityResult.GALERIA.getCodigoActivity())
        }catch (e : Exception){
            e.printStackTrace()
        }
    }




    private var escuchadorClick : (()->Unit) ?= null
    fun conEscuchadorClick(escuchadorClick : (()->Unit)) : BotonGaleria{
        this.escuchadorClick = escuchadorClick
        return this
    }

    private var escuchadorImagenCargada : ((Bitmap)->Unit) ?= null
    fun conEscuchadorImagenCargada(escuchadorImagenCargada : ((Bitmap)->Unit)) : BotonGaleria{
        this.escuchadorImagenCargada = escuchadorImagenCargada
        return this
    }

    fun conOnRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) :BotonGaleria{
        ManejadorPermisosGaleria
            .getInstancia()
            .conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        return this
    }

    private var appCompatActivity : AppCompatActivity ?= null
    fun conActivity(appCompatActivity : AppCompatActivity ):BotonGaleria{
        this.appCompatActivity = appCompatActivity
        return this
    }

    fun escuchaOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) : BotonGaleria
    {
        if (requestCode != CodigosActivityResult.GALERIA.getCodigoActivity()){ return this }
        if(resultCode != Activity.RESULT_OK && data == null ){ return this}

        RotadorImagenesGaleria()
            .conContext(context)
            .conRutaImagenCamara(data!!.data!!.toString())
            .conEscuchadorImagenRotada {
                escuchadorImagenCargada?.invoke(it)
            }
            .rotarImagen()

        mostreActivity = false
        return this
    }
}