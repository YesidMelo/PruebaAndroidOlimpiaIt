package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla5

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.mitiempo.pruebaolimpiaitandroid.Modelos.DetalleUsuario
import com.mitiempo.pruebaolimpiaitandroid.R

class GuardarInformacion @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)
{
    private var usuario : DetalleUsuario?= null
    fun conUsuario(usuario : DetalleUsuario) : GuardarInformacion{
        this.usuario = usuario
        return this
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla5,this,true)
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