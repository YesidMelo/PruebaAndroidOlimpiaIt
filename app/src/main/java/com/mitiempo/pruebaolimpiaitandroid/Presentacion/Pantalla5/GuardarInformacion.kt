package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla5

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mitiempo.pruebaolimpiaitandroid.R

class GuardarInformacion @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)
{
    init {
        LayoutInflater.from(context).inflate(R.layout.pantalla5,this,true)
    }
}