package com.mitiempo.pruebaolimpiaitandroid.Utilidades

enum class EtiquetasDialogo {
    DialogoGenerico;

    fun traerEtiqueta() : String{
        return when(this){
            DialogoGenerico-> "Dialogo Generico"
        }
    }
}