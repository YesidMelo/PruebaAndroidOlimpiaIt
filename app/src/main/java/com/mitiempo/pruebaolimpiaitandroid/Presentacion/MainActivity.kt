package com.mitiempo.pruebaolimpiaitandroid.Presentacion

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
        hola_mundo.setOnClickListener {
            mostrarDialogoDetallado(
                R.string.almacenamiento_nube,
                R.string.error_de_conexion,
                DialogoGenerico.TipoDialogo.ERROR
            )
        }
    }
}
