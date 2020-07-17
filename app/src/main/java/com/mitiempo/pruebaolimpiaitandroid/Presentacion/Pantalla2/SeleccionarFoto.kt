package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla2

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.mitiempo.pruebaolimpiaitandroid.Modelos.DetalleUsuario
import com.mitiempo.pruebaolimpiaitandroid.R
import kotlinx.android.synthetic.main.pantalla2.view.*

class SeleccionarFoto @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var usuario : DetalleUsuario?= null
    fun conUsuario(usuario : DetalleUsuario) : SeleccionarFoto{
        this.usuario = usuario
        return this
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla2,this,true)
        poneActivityValidoEnBotones()
        ponerEscuchadores()
    }

    private fun poneActivityValidoEnBotones(){
        boton_camara.conActivity(context as AppCompatActivity)
        boton_galeria.conActivity(context as AppCompatActivity)
    }

    private fun ponerEscuchadores(){
        ponerEscuchadorCamara()
        ponerEscuchadorGaleria()
    }

    private fun ponerEscuchadorCamara(){
        boton_camara
            .conEscuchadorImagenCargada {
                if (it == null ){ return@conEscuchadorImagenCargada }
                foto_seleccionada.setImageBitmap(it)
            }
    }

    private fun ponerEscuchadorGaleria(){
        boton_galeria
            .conEscuchadorImagenCargada {
                foto_seleccionada.setImageBitmap(it)
            }
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



    fun conOnRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) :SeleccionarFoto {
        boton_camara.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        boton_galeria.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        return this
    }

    fun escuchaOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) : SeleccionarFoto{
        boton_camara.escuchaOnActivityResult(requestCode, resultCode, data)
        boton_galeria.escuchaOnActivityResult(requestCode, resultCode, data)
        return this
    }
}