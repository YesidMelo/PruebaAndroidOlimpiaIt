package com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley

import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mitiempo.pruebaolimpiaitandroid.BuildConfig
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.ProxyVolley.Servicios
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProxyVolley {

    interface VolleyParcelable
    interface VolleyService {

        fun getMethod(): Metodo
        fun complemento(): String?
        fun getURL(): String
        fun getNombreDocumentoMock(): String?


        companion object {
            var esMock = false
        }

    }


    private var context: Context? = null
    fun conContexto(context: Context): ProxyVolley {
        this.context = context
        return this
    }

    private var objetoAEnviar: VolleyParcelable? = null
    fun <T : VolleyParcelable> conObjetoAEnviar(objetoAEnviar: T): ProxyVolley {
        this.objetoAEnviar = objetoAEnviar
        return this
    }

    private var claseEsperada: Class<*>? = null
    fun <K : VolleyParcelable> conObjetoEsperado(objetoEsperado: Class<K>): ProxyVolley {
        this.claseEsperada = objetoEsperado
        return this
    }

    private var escuchadorRespuestaExitosa: ((VolleyParcelable?, MutableList<VolleyParcelable>?) -> Unit)? =
        null

    fun conEscuchadorRespuestaExitosa(escuchadorRespuestaExitosa: ((VolleyParcelable?, MutableList<VolleyParcelable>?) -> Unit)): ProxyVolley {
        this.escuchadorRespuestaExitosa = escuchadorRespuestaExitosa
        return this
    }

    private var escuchadorRespuestaFallida: ((Int, Int) -> Unit)? = null
    fun conEscuchadorRespuestaFallida(escuchadorRespuestaFallida: ((Int, Int) -> Unit)): ProxyVolley {
        this.escuchadorRespuestaFallida = escuchadorRespuestaFallida
        return this
    }

    private var servicio: VolleyService? = null
    fun conServicio(servicio: Servicios): ProxyVolley {
        this.servicio = servicio
        return this
    }

    private var mostrandoValoresNulos = false
    private fun mostrarValoresNulos(mostrar: Boolean = false): ProxyVolley {
        this.mostrandoValoresNulos = mostrar
        return this
    }

    companion object {

        private var requestQueue: RequestQueue? = null

    }


    fun realizarConsulta(): ProxyVolley {

        if (!estanTodosLosParametros()) {
            return this
        }

        GlobalScope.launch {
            iniciarConsulta()
        }
        return this
    }


    class contextError :
        Exception("ProxyVolley no tiene context  para continuar con su funcionamiento por favor  verifique la construccion de ProxyVolley")

    class objetoAEnviarError :
        Exception("ProxyVolley no tiene objetoAEnviar  para continuar con su funcionamiento por favor  verifique la construccion de ProxyVolley")

    class claseEsperadaError :
        Exception("ProxyVolley no tiene claseEsperada  para continuar con su funcionamiento por favor  verifique la construccion de ProxyVolley")

    class escuchadorRespuestaExitosaError :
        Exception("ProxyVolley no tiene escuchadorRespuestaExitosa  para continuar con su funcionamiento por favor  verifique la construccion de ProxyVolley")

    class escuchadorRespuestaFallidaError :
        Exception("ProxyVolley no tiene escuchadorRespuestaFallida  para continuar con su funcionamiento por favor  verifique la construccion de ProxyVolley")

    class servicioError :
        Exception("ProxyVolley no tiene servicio  para continuar con su funcionamiento por favor  verifique la construccion de ProxyVolley")

    private fun estanTodosLosParametros(): Boolean {

        if (context == null) {
            throw contextError()
        }
        if (objetoAEnviar == null) {
            throw objetoAEnviarError()
        }
        if (claseEsperada == null) {
            throw claseEsperadaError()
        }
        if (escuchadorRespuestaExitosa == null) {
            throw escuchadorRespuestaExitosaError()
        }
        if (escuchadorRespuestaFallida == null) {
            throw escuchadorRespuestaFallidaError()
        }
        if (servicio == null) {
            throw servicioError()
        }

        return true
    }


    private val baseURL = BuildConfig.Servidor
    private val T = "ProxyVolley"
    private fun iniciarConsulta() {

        NukeSSLCerts.nuke()
        val url = generarUrl()
        Log.e("Url", " url -> " + url)
        val stringRequest = generarStringRequest(url)

        adicionarTiempoDeEspera(stringRequest)

        requestQueue = Volley.newRequestQueue(context!!)
        requestQueue?.cache?.clear()
        requestQueue?.add(stringRequest)

    }

    private fun generarUrl(): String {
        return baseURL + "" + servicio!!.getURL() + if (servicio!!.complemento() != null) servicio!!.complemento() else ""
    }

    private fun generarStringRequest(url: String): StringRequest {
        val escuchador = EscuchadorRespuestaVolley()
            .conEscuchadorRespuestaExitosa(escuchadorRespuestaExitosa!!)
            .conEscuchadorRespuestaFallida(escuchadorRespuestaFallida!!)
            .conClase(claseEsperada!!)

        return object : StringRequest(
            servicio!!.getMethod().getValueByVolley(),
            url,
            escuchador
            ,
            ErrorResponseVolley()
                .conEscuchadorRespuestaFallida(escuchadorRespuestaFallida!!)

        ) {
            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getBody(): ByteArray? {
                return generarObjetoAEnviar()?.toByteArray()
            }

            private fun generarObjetoAEnviar(): String? {
                if (objetoAEnviar == null) {
                    return null
                }
                val json = if (!mostrandoValoresNulos) {
                    Gson().toJson(objetoAEnviar)

                } else {
                    GsonBuilder().setPrettyPrinting().serializeNulls().create()
                        .toJson(objetoAEnviar)
                }

                Log.e(T, "json : " + json)
                if (json == null || json.equals("null")) {
                    return null
                }
                return json
            }


            override fun getHeaders(): MutableMap<String, String> {
                val manejadorCabezera = ManejadorDeCabezerasDePeticiones()
                    .conContexto(context!!)
                    .conMetodo(servicio!!.getMethod())
                    .generarCabezeras()

                return manejadorCabezera.traerCabezeras()
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {

                escuchador
                    .conRespuestaDeRed(response)

                return super.parseNetworkResponse(response)
            }

            override fun parseNetworkError(volleyError: VolleyError?): VolleyError {
                return super.parseNetworkError(volleyError)
            }

        }
    }


    private var tiempoDeEspera = 30_000
    private fun adicionarTiempoDeEspera(stringRequest: StringRequest) {
        stringRequest.setRetryPolicy(

            DefaultRetryPolicy(
                tiempoDeEspera,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

        )
    }


}