package com.mitiempo.pruebaolimpiaitandroid.DataAccess.Repositorios

import android.content.Context
import com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley.ProxyVolley
import com.mitiempo.pruebaolimpiaitandroid.Modelos.DetalleUsuario
import com.mitiempo.pruebaolimpiaitandroid.Modelos.ModeloBase
import com.mitiempo.pruebaolimpiaitandroid.Modelos.ProxyVolley.MensajeError
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.ProxyVolley.Servicios

class RepoUsuario(private val context: Context) {

    private var escuchadorFalla : ((Int,Int)->Unit) ?= null
    fun conEscuchadorFalla(escuchadorFalla : ((Int,Int)->Unit)) : RepoUsuario{
        this.escuchadorFalla = escuchadorFalla
        return this
    }

    private var escuchadorExitoObjeto : ((ModeloBase?)->Unit) ?= null
    fun conEscuchadorExitoObjeto(escuchador : ((ModeloBase?)->Unit) ) :  RepoUsuario{
        this.escuchadorExitoObjeto = escuchador
        return this
    }

    private var escuchadorExitoListaObjetos : ((MutableList<ModeloBase>?)->Unit) ?= null
    fun conEscuchadorExitoListaObjetos(escuchador : ((MutableList<ModeloBase>?)->Unit) ) :  RepoUsuario{
        this.escuchadorExitoListaObjetos = escuchador
        return this
    }

    fun enviarUsuario(usuario: DetalleUsuario){
        ProxyVolley()
            .conContexto(context)
            .conEscuchadorRespuestaExitosa { objeto, listaobjetos ->
               escuchadorExitoObjeto?.invoke(objeto as? ModeloBase )
               escuchadorExitoListaObjetos?.invoke(listaobjetos as? MutableList<ModeloBase>)
            }
            .conEscuchadorRespuestaFallida { titulo, mensaje ->
                escuchadorFalla?.invoke(titulo,mensaje)
            }
            .conObjetoAEnviar(usuario)
            .conObjetoEsperado(MensajeError::class.java)
            .conServicio(Servicios.GUARDAR_USUARIO)
            .realizarConsulta()
    }

}