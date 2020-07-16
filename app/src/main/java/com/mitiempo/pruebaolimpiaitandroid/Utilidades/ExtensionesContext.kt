package com.mitiempo.pruebaolimpiaitandroid.Utilidades

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R


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
