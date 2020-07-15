package com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley

import android.util.Log
import com.android.volley.Response
import com.android.volley.VolleyError
import com.mitiempo.pruebaolimpiaitandroid.BuildConfig
import com.mitiempo.pruebaolimpiaitandroid.Modelos.ProxyVolley.MensajeError
import com.mitiempo.pruebaolimpiaitandroid.R
import com.google.gson.Gson

class ErrorResponseVolley : Response.ErrorListener {

    private val T = "ErrorResponseVolley"
    override fun onErrorResponse(error: VolleyError?) {
        try {
            val mensajeJson = String(error!!.networkResponse.data)
            gestorMensajesError(mensajeJson)
        } catch (e: Exception) {
            Log.e(T, "", e)
            escuchadorRespuestaFallida?.invoke(
                R.string.error_de_conexion,
                R.string.surgio_un_problema_durante_la_conexion
            )
        }


    }


    private fun gestorMensajesError(mensaje: String) {
        try {

            val mensajeRecibido = Gson().fromJson(mensaje, MensajeError::class.java)
            imprimirMensajeError(mensajeRecibido)
            escuchadorRespuestaFallida?.invoke(
                R.string.error_de_conexion,
                R.string.surgio_un_problema_durante_la_conexion
            )

        } catch (e: Exception) {

            Log.e(T, "Mensaje Recibido: " + mensaje, e)
            escuchadorRespuestaFallida?.invoke(
                R.string.error_de_conexion,
                R.string.surgio_un_problema_durante_la_conexion
            )

        }

    }

    private fun imprimirMensajeError(mensajeRecibido: MensajeError) {
        if (!BuildConfig.MostrarToken) {
            return
        }
        try {
            Log.e(T, mensajeRecibido.message)
        } catch (e: Exception) {
            Log.e(T, "", e)
        }
    }

    private var escuchadorRespuestaFallida: ((Int, Int) -> Unit)? = null
    fun conEscuchadorRespuestaFallida(escuchadorRespuestaFallida: ((Int, Int) -> Unit)): ErrorResponseVolley {
        this.escuchadorRespuestaFallida = escuchadorRespuestaFallida
        return this
    }


}