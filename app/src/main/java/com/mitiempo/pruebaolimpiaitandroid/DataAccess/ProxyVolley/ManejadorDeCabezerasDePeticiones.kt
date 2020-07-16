package com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley

import android.content.Context
import android.util.Log
import com.mitiempo.pruebaolimpiaitandroid.BuildConfig
import com.mitiempo.pruebaolimpiaitandroid.Modelos.ProxyVolley.UsuarioToken
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.ProxyVolley.IdentificadorSharedPreferences

class ManejadorDeCabezerasDePeticiones {

    private var contexto: Context? = null
    fun conContexto(context: Context): ManejadorDeCabezerasDePeticiones {
        this.contexto = context
        return this
    }

    private var metodo: Metodo = Metodo.GET
    fun conMetodo(metodo: Metodo): ManejadorDeCabezerasDePeticiones {
        this.metodo = metodo
        return this
    }

    private val mapaCabezeras = emptyMap<String, String>().toMutableMap()
    fun generarCabezeras(): ManejadorDeCabezerasDePeticiones {
        mapaCabezeras["Content-Type"] = "application/json"
        verificaElMetodoDeEnvio()
        verificarToken()
        return this
    }

    private fun verificarToken() {
        PreferenciasServidor(contexto!!)
            .conEscuchadorExito { objeto, _ ->
                if (objeto == null) {
                    return@conEscuchadorExito
                }
                val usuario = objeto as UsuarioToken

                if (usuario.token == null) {
                    return@conEscuchadorExito
                }
                mapaCabezeras["Authorization"] = usuario.token!!

                if (BuildConfig.MostrarToken) {
                    Log.e("Token : ", usuario.token!!)
                }

            }
            .conEscuchadorFalla { _, _ ->
                Log.e("manejador cabezera", "Fallo la carga del token")
            }
            .traerObjeto(UsuarioToken::class.java, IdentificadorSharedPreferences.Token)
    }

    private fun verificaElMetodoDeEnvio() {
        if (metodo == Metodo.PATCH) {
            mapaCabezeras["X-HTTP-Method-Override"] = "PATCH"
        }
    }

    fun traerCabezeras(): MutableMap<String, String> {
        return mapaCabezeras
    }

}