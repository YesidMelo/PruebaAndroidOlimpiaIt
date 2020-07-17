package com.mitiempo.pruebaolimpiaitandroid.Modelos.ProxyVolley

import com.google.gson.annotations.SerializedName
import com.mitiempo.pruebaolimpiaitandroid.Modelos.ModeloBase

class MensajeError : ModeloBase {

    var title: String? = null
    @SerializedName("codigo")
    var code: Int? = null
    @SerializedName("mensaje")
    var message: String? = null
}