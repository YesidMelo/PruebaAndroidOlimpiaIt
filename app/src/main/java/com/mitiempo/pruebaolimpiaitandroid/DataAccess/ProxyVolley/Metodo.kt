package com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley

import com.android.volley.Request

enum class Metodo {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    ;

    fun getValueByVolley(): Int {
        return when (this) {
            GET -> Request.Method.GET
            POST -> Request.Method.POST
            PUT -> Request.Method.PUT
            DELETE -> Request.Method.DELETE
            PATCH -> Request.Method.PATCH
        }
    }
}