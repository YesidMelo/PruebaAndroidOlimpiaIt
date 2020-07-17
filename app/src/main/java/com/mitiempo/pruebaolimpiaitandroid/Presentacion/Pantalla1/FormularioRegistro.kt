package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla1

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.mitiempo.pruebaolimpiaitandroid.Modelos.DetalleUsuario
import com.mitiempo.pruebaolimpiaitandroid.R
import kotlinx.android.synthetic.main.pantalla1.view.*

class FormularioRegistro @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {




    private var usuario : DetalleUsuario ?= null
    fun conUsuario(usuario : DetalleUsuario) : FormularioRegistro{
        this.usuario = usuario
        return this
    }

    private var escuchadorSiguiente : (()->Unit) ?= null
    fun conEscuchadorSiguiente(escuchadorSiguiente : (()->Unit)) : FormularioRegistro{
        this.escuchadorSiguiente = escuchadorSiguiente
        return this
    }

    fun mostrarVista(){
        if (usuario == null ){ return }

        post {
            visibility = View.VISIBLE
        }
    }

    fun ocultarVista(){
        post{
            visibility = View.GONE
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla1,this,true)
        ponerEscuchadores()
    }

    private fun ponerEscuchadores() {
        boton_siguiente_registro.setOnClickListener {
            actualizarElementosUsuario()
            escuchadorSiguiente?.invoke()
        }
    }

    private fun actualizarElementosUsuario() {
        if(usuario == null ){ return }

        usuario!!.nombre = edittext_nombre.text.toString()
        usuario!!.cedula = editext_cedula.text.toString()
        usuario!!.direccion = edittext_direccion.text.toString()
        usuario!!.ciudad = edittext_direccion.text.toString()
        usuario!!.pais = edittext_pais.text.toString()
        usuario!!.celular = editext_celular.text.toString()


    }

}