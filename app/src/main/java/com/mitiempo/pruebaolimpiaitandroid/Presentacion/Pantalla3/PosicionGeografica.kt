package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla3

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.mitiempo.pruebaolimpiaitandroid.Modelos.DetalleUsuario
import com.mitiempo.pruebaolimpiaitandroid.Modelos.GPS
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoDetallado
import kotlinx.android.synthetic.main.pantalla3.view.*

class PosicionGeografica @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var usuario : DetalleUsuario?= null
    fun conUsuario(usuario : DetalleUsuario) : PosicionGeografica {
        this.usuario = usuario
        return this
    }

    private var escuchadorSiguiente : (()->Unit) ?= null
    fun conEscuchadorSiguiente(escuchadorSiguiente : (()->Unit)) : PosicionGeografica{
        this.escuchadorSiguiente = escuchadorSiguiente
        return this
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla3,this,true)
        ponerEscuchadores()
    }

    private fun ponerEscuchadores() {

        boton_siguiente_gps.setOnClickListener {
            escuchadorSiguiente?.invoke()
        }
    }

    fun mostrarVista(){
        post {
            visibility = View.VISIBLE
            verificarPermisos()
        }
    }

    private var manejadorPermisosGPS : ManejadorPermisosGPS ?= null
    private fun verificarPermisos(){

        if(manejadorPermisosGPS == null ){
            manejadorPermisosGPS = ManejadorPermisosGPS(context)
        }

        manejadorPermisosGPS
            ?.conEscuchadorRespuestaNegativaDialogo {
                context.mostrarDialogoDetallado(
                    R.string.GPS,
                    R.string.no_habilito_los_permisos_gps,
                    DialogoGenerico.TipoDialogo.ERROR
                )
            }
            ?.conEscuchadorRespuestaPositivaDialogo {
                inicializarManejadorGoogleMaps()
                inicializaLocalizacionGPS()

            }
            ?.verificarPermisos()
    }

    private var manejadorGoogleMap : ManejadorGoogleMap ?= null
    private var mapa : SupportMapFragment ?= null
    private fun inicializarManejadorGoogleMaps(){

        if(manejadorGoogleMap == null ){
            mapa = (context as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
            manejadorGoogleMap = ManejadorGoogleMap(context,mapa!!)
        }

        manejadorGoogleMap
            ?.adicionarCoordenadas()
            ?.actualizarMapa()
    }

    private var manejadorGPS : ManejadorGPS ?= null
    private fun inicializaLocalizacionGPS(){
        if(manejadorGPS != null ){ return }
        manejadorGPS = ManejadorGPS(context)
            .conEscuchadorCoordenadas {
                manejadorGoogleMap
                    ?.adicionarCoordenadas(it,context.getString(R.string.mi_posicion_actual))
                    ?.actualizarMapa()
                actualizarPocicionUsuario(it)
            }
            .onStart()
    }

    private fun actualizarPocicionUsuario(posicion: LatLng) {
        if (visibility != View.VISIBLE){ return }

        val gps = GPS()
        gps.Latitud = posicion.latitude.toString()
        gps.Longitud = posicion.longitude.toString()
        usuario?.GPS = gps

    }

    fun onStart(): PosicionGeografica{
        manejadorGPS?.onStart()
        return this
    }

    fun onResume(): PosicionGeografica{
        manejadorGPS?.onResume()
        return this
    }

    fun onPause(): PosicionGeografica{
        manejadorGPS?.onPause()
        return this
    }

    fun ocultarVista(){
        post {
            visibility = View.GONE
        }
    }

    fun conOnRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) : PosicionGeografica{
        manejadorPermisosGPS
            ?.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        return this
    }
}