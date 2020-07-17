package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla5

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.mitiempo.pruebaolimpiaitandroid.DataAccess.Repositorios.RepoUsuario
import com.mitiempo.pruebaolimpiaitandroid.Modelos.DetalleUsuario
import com.mitiempo.pruebaolimpiaitandroid.Modelos.ProxyVolley.MensajeError
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoDetallado
import kotlinx.android.synthetic.main.pantalla5.view.*

class GuardarInformacion @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)
{
    private var usuario : DetalleUsuario?= null
    fun conUsuario(usuario : DetalleUsuario) : GuardarInformacion{
        this.usuario = usuario
        return this
    }

    private var escuchadorSiguiente : (()->Unit) ?= null
    fun conEscuchadorSiguiente(escuchadorSiguiente : (()->Unit)) : GuardarInformacion{
        this.escuchadorSiguiente = escuchadorSiguiente
        return this
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla5,this,true)
        ponerEscuchadores()
    }

    private fun ponerEscuchadores() {
        boton_guardar.setOnClickListener {
            enviarInformacionAlServidor()
        }
    }

    private fun enviarInformacionAlServidor() {
        if (usuario == null ){ return }

        RepoUsuario(context)
            .conEscuchadorExitoObjeto {

                val mensajeError = it as? MensajeError


                context.mostrarDialogoDetallado(
                    R.string.guardar,
                    R.string.se_ha_enviado_la_informacion_al_servidor_exitosamente,
                    DialogoGenerico.TipoDialogo.OK
                )
            }
            .conEscuchadorFalla { titulo, mensaje ->
                context.mostrarDialogoDetallado(
                    titulo,
                    mensaje,
                    DialogoGenerico.TipoDialogo.ERROR
                )
            }
            .enviarUsuario(this.usuario!!)
    }

    fun mostrarVista(){
        post {
            visibility = View.VISIBLE
        }
    }

    fun ocultarVista(){
        post {
            visibility = View.GONE
        }
    }
}