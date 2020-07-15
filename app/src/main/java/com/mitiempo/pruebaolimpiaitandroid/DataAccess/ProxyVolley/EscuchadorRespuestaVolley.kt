package com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley

import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class EscuchadorRespuestaVolley : Response.Listener<String> {

    override fun onResponse(response: String?) {
//        esta linea contiene el http code para clasificar las respuestas
//        this.response?.statusCode
        generateResponse200(response)

    }


    private fun generateResponse200(response: String?) {


        GlobalScope.launch {
            if (generateWithJSONEmpty(response)) {
                return@launch
            }
            if (generateResponseWithUniqueObject(response!!)) {
                return@launch
            }
            if (generateResponseWithArrayObject(response)) {
                return@launch
            }

            escuchadorRespuestaExitosa?.invoke(null, null)
        }

    }

    private fun generateWithJSONEmpty(response: String?): Boolean {

        if (response.isNullOrEmpty()) {
            escuchadorRespuestaExitosa?.invoke(null, null)
            return true
        }
        return false

    }

    private fun generateResponseWithUniqueObject(response: String): Boolean {
        return try {
            val myObject = Gson().fromJson(response, clase)
            escuchadorRespuestaExitosa?.invoke(myObject as ProxyVolley.VolleyParcelable, null)
            true
        } catch (e: JsonSyntaxException) {
            false
        }
    }

    private fun generateResponseWithArrayObject(response: String): Boolean {
        return try {

            val type = object : TypeToken<MutableList<Any>>() {}.getType()
            val myArayObj = Gson().fromJson<MutableList<Any>>(response, type)

            val finalList = emptyList<ProxyVolley.VolleyParcelable>().toMutableList()

            myArayObj.forEach { obj ->
                val myObj = Gson().fromJson(Gson().toJson(obj), clase)
                finalList.add(myObj as ProxyVolley.VolleyParcelable)
            }

            escuchadorRespuestaExitosa?.invoke(null, finalList)
            true
        } catch (e: JsonSyntaxException) {
            Log.e("Error", "", e)
            false
        }
    }


    private var escuchadorRespuestaExitosa: ((ProxyVolley.VolleyParcelable?, MutableList<ProxyVolley.VolleyParcelable>?) -> Unit)? =
        null

    fun conEscuchadorRespuestaExitosa(escuchadorRespuestaExitosa: ((ProxyVolley.VolleyParcelable?, MutableList<ProxyVolley.VolleyParcelable>?) -> Unit)): EscuchadorRespuestaVolley {
        this.escuchadorRespuestaExitosa = escuchadorRespuestaExitosa
        return this
    }

    private var escuchadorRespuestaFallida: ((Int, Int) -> Unit)? = null
    fun conEscuchadorRespuestaFallida(escuchadorRespuestaFallida: ((Int, Int) -> Unit)): EscuchadorRespuestaVolley {
        this.escuchadorRespuestaFallida = escuchadorRespuestaFallida
        return this
    }

    private var clase: Class<*>? = null
    fun conClase(clase: Class<*>): EscuchadorRespuestaVolley {
        this.clase = clase
        return this
    }

    private var response: NetworkResponse? = null
    fun conRespuestaDeRed(response: NetworkResponse?): EscuchadorRespuestaVolley {
        this.response = response
        return this
    }

}