package com.mitiempo.pruebaolimpiaitandroid.Presentacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos.DialogoGenerico
import com.mitiempo.pruebaolimpiaitandroid.R
import com.mitiempo.pruebaolimpiaitandroid.Utilidades.mostrarDialogoDetallado
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread{
            Thread.sleep(5_000)
            pocision_geografica.mostrarVista()
        }.start()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        selector_foto.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
        pocision_geografica.conOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        selector_foto.escuchaOnActivityResult(requestCode, resultCode, data)
    }

}
