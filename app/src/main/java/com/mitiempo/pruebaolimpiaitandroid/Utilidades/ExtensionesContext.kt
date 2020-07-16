package com.mitiempo.pruebaolimpiaitandroid.Utilidades

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


private fun esUnContextoValidoParaMostrarMensaje(contex: Context) : Boolean{
    return contex is AppCompatActivity || contex is ContextWrapper
}

private fun traerContextValido(contex : Context) : Context{
    var finalContex : Context = contex
    while((finalContex !is AppCompatActivity) && (finalContex is ContextWrapper)){
        finalContex = (finalContex as ContextWrapper).baseContext
    }
    return finalContex
}

fun Context.mostrarDialogoGenerico(){
    if(!esUnContextoValidoParaMostrarMensaje(this)){ return }
    if(this !is AppCompatActivity){
        traerContextValido(this).mostrarDialogoGenerico()
        return
    }
    runOnUiThread {
        DialogoGenerico.getInstancia().mostrarDialogo(supportFragmentManager,EtiquetasDialogo.DialogoGenerico.traerEtiqueta())
    }
}

fun Context.mostrarDialogoDetallado(
    titulo : Int,
    mensaje : Int,
    tipoDialogo: DialogoGenerico.TipoDialogo,
    accionBoton1 : (()->Unit)?= null,
    accionBoton2 : (()->Unit)?= null,
    mostrarBotonCancelar : Boolean = false
){
    DialogoGenerico
        .getInstancia()
        .conTexto(mensaje)
        .conTitulo(titulo)
        .conTipoDialogo(tipoDialogo)
        .conAccionBoton1 {
            accionBoton1?.invoke()
        }
        .conAccionBoton2 {
            accionBoton2?.invoke()
        }

    if (mostrarBotonCancelar){
        DialogoGenerico
            .getInstancia()
            .conTextoBoton2(R.string.cancelar)
    }

    mostrarDialogoGenerico()
}

//imagenes

@SuppressLint("SimpleDateFormat")
fun Context.crearDocumentoImagen() : File{
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName: String = "JPEG_" + timeStamp + "_"
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    if (!storageDir.exists()) storageDir.mkdirs()
    val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
    return imageFile
}