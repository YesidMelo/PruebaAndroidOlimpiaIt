package com.mitiempo.pruebaolimpiaitandroid.Utilidades.ProxyVolley

import com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley.PreferenciasServidor

enum class IdentificadorSharedPreferences(private val identificador: String) :
    PreferenciasServidor.PreferenciasServidorIdentificador {
    Token("Token");

    override fun traerIdentificador(): String {
        return identificador
    }
}