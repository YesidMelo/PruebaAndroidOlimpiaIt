package com.mitiempo.pruebaolimpiaitandroid.Presentacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mitiempo.pruebaolimpiaitandroid.Modelos.DetalleUsuario
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoDetallado
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pantalla5.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializarManejadorNavegacion()
    }

    private var manejadorNavegacion : ManejadorNavegacion ?= null
    private fun inicializarManejadorNavegacion(){
        if(manejadorNavegacion != null ){ return }
        manejadorNavegacion = ManejadorNavegacion(this)
            .conFormularioRegistro(formulario_registro)
            .conPosicionGeografica(pocision_geografica)
            .conSeleccionarFoto(selector_foto)
            .conEstadosBluetoothWifi(verificador_estados_bluetooth_wifi)
            .conGuardarInformacion(guardar_informacion)
            .inicializarNavegacion()
    }

    override fun onStart() {
        super.onStart()
        pocision_geografica.onStart()
    }

    override fun onResume() {
        super.onResume()
        pocision_geografica.onResume()
    }

    override fun onPause() {
        super.onPause()
        pocision_geografica.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        verificador_estados_bluetooth_wifi.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        selector_foto.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        pocision_geografica.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        verificador_estados_bluetooth_wifi.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        selector_foto.escuchaOnActivityResult(requestCode, resultCode, data)
    }

}
