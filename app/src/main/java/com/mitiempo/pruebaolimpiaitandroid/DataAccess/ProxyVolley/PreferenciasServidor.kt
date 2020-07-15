package com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley

import android.content.Context
import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import com.mitiempo.pruebaolimpiaitandroid.R
import com.google.gson.Gson
import org.jetbrains.anko.defaultSharedPreferences

class PreferenciasServidor(
    private val context: Context
) {
    interface PreferenciasServidorParcelable
    interface PreferenciasServidorIdentificador {
        fun traerIdentificador(): String
    }

    private val T = "PreferenciasServidor"


    private var escuchadorExito: ((PreferenciasServidorParcelable?, MutableList<PreferenciasServidorParcelable>?) -> Unit)? =
        null

    fun conEscuchadorExito(escuchadorExito: ((PreferenciasServidorParcelable?, MutableList<PreferenciasServidorParcelable>?) -> Unit)): PreferenciasServidor {
        this.escuchadorExito = escuchadorExito
        return this
    }

    private var escuchadorFalla: ((Int, Int) -> Unit)? = null
    fun conEscuchadorFalla(escuchadorFalla: ((Int, Int) -> Unit)): PreferenciasServidor {
        this.escuchadorFalla = escuchadorFalla
        return this
    }

    fun <T : PreferenciasServidorParcelable, K : PreferenciasServidorIdentificador> guardarObjeto(
        objeto: T,
        identificador: K
    ) {
        try {
            val almacenador = context.defaultSharedPreferences.edit()
            almacenador.putString(identificador.traerIdentificador(), Gson().toJson(objeto))
            almacenador.commit()
            escuchadorExito?.invoke(objeto, null)
        } catch (e: Exception) {
            Log.e(T, "", e)
            escuchadorFalla?.invoke(R.string.falla, R.string.surgio_un_error_inesperado)
        }
    }

    fun <T : PreferenciasServidorParcelable, K : PreferenciasServidorIdentificador> actualizar(
        objeto: T,
        identificador: K
    ) {
        try {
            val almacenador = context.defaultSharedPreferences.edit()
            almacenador.putString(identificador.traerIdentificador(), Gson().toJson(objeto))
            almacenador.commit()
            escuchadorExito?.invoke(objeto, null)
        } catch (e: Exception) {
            Log.e(T, "", e)
            escuchadorFalla?.invoke(R.string.falla, R.string.surgio_un_error_inesperado)
        }
    }

    fun <T : PreferenciasServidorIdentificador> eliminar(identificador: T) {
        try {
            val almacenador = context.defaultSharedPreferences.edit()
            almacenador.remove(identificador.traerIdentificador())
            almacenador.commit()
            escuchadorExito?.invoke(null, null)
        } catch (e: Exception) {
            Log.e(T, "", e)
            escuchadorFalla?.invoke(R.string.falla, R.string.surgio_un_error_inesperado)
        }
    }

    fun <T : PreferenciasServidorParcelable, K : PreferenciasServidorIdentificador> traerObjeto(
        clase: Class<T>,
        identificador: K
    ) {
        try {
            val almacenador = context.defaultSharedPreferences
            val objeto = Gson().fromJson(
                almacenador.getString(identificador.traerIdentificador(), "{}"),
                clase
            )
            escuchadorExito?.invoke(objeto, null)
        } catch (e: Exception) {
            Log.e(T, "", e)
            escuchadorFalla?.invoke(R.string.falla, R.string.surgio_un_error_inesperado)
        }

    }


}