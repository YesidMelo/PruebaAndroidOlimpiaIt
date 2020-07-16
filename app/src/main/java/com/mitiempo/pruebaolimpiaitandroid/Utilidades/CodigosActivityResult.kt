package com.mitiempo.pruebaolimpiaitandroid.Utilidades

enum class CodigosActivityResult (private val codigo : Int, private val resultado : Int) {

    GALERIA(1,1),
    CAMARA(2,2),
    GPS(3,3),
    ;
    fun getCodigoActivity() : Int{
        return codigo
    }

    fun getPermissionResult() : Int{
        return resultado
    }

}