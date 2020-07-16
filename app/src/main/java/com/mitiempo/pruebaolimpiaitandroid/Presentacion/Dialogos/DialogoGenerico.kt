package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Dialogos

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mitiempo.pruebaolimpiaitandroid.R

class DialogoGenerico private constructor() : DialogFragment() {

    private val T = "DialogoGenerico"

    companion object{
        private var mostrandoElDialogo = false
        private var instancia : DialogoGenerico?= null

        fun getInstancia() : DialogoGenerico {
            if(instancia == null )
            {
                instancia =
                    DialogoGenerico()
            }
            return instancia!!
        }
    }



    private var contenedorPrincipal : View?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contenedorPrincipal = inflater.inflate(R.layout.dialogo_generico,null,false)
        isCancelable = false

        encuentraElementosVista()
        llenaVista()
        adicionarEscuchadores()
        return contenedorPrincipal
    }

    private var imagen_dialogo : ImageView?= null
    private var detalle_mensaje : TextView?= null
    private var Titulo : TextView?= null
    private var boton1_dialogo : Button?= null
    private var boton2_dialogo : Button?= null
    private var linea_guia : Guideline?= null


    private fun encuentraElementosVista(){

        imagen_dialogo      = contenedorPrincipal?.findViewById(R.id.imagen_dialogo )
        detalle_mensaje     = contenedorPrincipal?.findViewById(R.id.detalle_mensaje )
        Titulo      = contenedorPrincipal?.findViewById(R.id.Titulo )
        boton1_dialogo      = contenedorPrincipal?.findViewById(R.id.boton1_dialogo )
        boton2_dialogo      = contenedorPrincipal?.findViewById(R.id.boton2_dialogo )
        linea_guia      = contenedorPrincipal?.findViewById(R.id.linea_guia )

    }


    private fun llenaVista(){
        seleccionarIcono()
        llenarTitulo()
        llenarMensaje()
        llenarBoton1()
        llenarBoton2()
    }

    private fun seleccionarIcono(){
        imagen_dialogo?.setImageResource(tipoDialogo.getIcono())
    }

    private fun llenarTitulo(){
        if(rutaTitulo == null ){
            Titulo?.visibility = View.GONE
            return
        }
        Titulo?.visibility = View.VISIBLE
        Titulo?.setText(rutaTitulo!!)
    }

    private fun llenarMensaje(){
        if(rutaTexto == null ){
            detalle_mensaje?.visibility = View.GONE
            return
        }
        detalle_mensaje?.visibility = View.VISIBLE
        detalle_mensaje?.setText(rutaTexto!!)
    }

    private fun llenarBoton1(){
        if(rutaTextoBoton1 == null ){
            boton1_dialogo?.setText(R.string.aceptar)
            return
        }
        boton1_dialogo?.setText(rutaTextoBoton1!!)
    }

    private fun llenarBoton2(){
        if(rutaTextoBoton2 == null ){
            boton2_dialogo?.visibility = View.GONE
            linea_guia?.setGuidelinePercent(1.toFloat())
            return
        }
        linea_guia?.setGuidelinePercent((0.5).toFloat())
        boton2_dialogo?.visibility = View.VISIBLE
        boton2_dialogo?.setText(rutaTextoBoton2!!)
    }


    private fun adicionarEscuchadores(){
        boton1_dialogo?.setOnClickListener {
            dismiss()
            invocaAccion1?.invoke()
            limpiarElementosDeVista()
        }
        boton2_dialogo?.setOnClickListener {
            dismiss()
            invocaAccion2?.invoke()
            limpiarElementosDeVista()
        }
    }

    private fun limpiarElementosDeVista(){
        rutaTextoBoton2 = null
        invocaAccion1 = null
        invocaAccion2 =  null
    }

    override fun dismiss() {
        mostrandoElDialogo = false
        if(fragmentManager == null ){
            return
        }
        super.dismiss()
        super.dismissAllowingStateLoss()
    }


    override fun onStart() {
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        super.onStart()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if(mostrandoElDialogo){ return }
        if(isAdded){ return }
        mostrandoElDialogo = true
        super.show(manager, tag)

    }






    enum class TipoDialogo{
        ERROR,
        ADVERTENCIA,
        OK
        ;

        fun getIcono(): Int{
            return when(this){
                OK -> R.drawable.ic_check
                ADVERTENCIA -> R.drawable.ic_warning
                ERROR -> R.drawable.ic_close
            }
        }
    }

    private var invocaAccion1:(()->Unit)?= null
    fun conAccionBoton1(accion1 : ()->Unit) : DialogoGenerico {
        this.invocaAccion1 = accion1
        return this
    }

    private var invocaAccion2 : (()->Unit)?= null
    fun conAccionBoton2(accion2 : ()->Unit) : DialogoGenerico {
        this.invocaAccion2 = accion2
        return this
    }

    private @StringRes
    var rutaTitulo : Int ?= null
    fun conTitulo(@StringRes rutaString : Int): DialogoGenerico {
        this.rutaTitulo = rutaString
        return this
    }

    private @StringRes
    var rutaTexto : Int ?= null
    fun conTexto(@StringRes rutaString : Int): DialogoGenerico {
        this.rutaTexto = rutaString
        return this
    }

    private @StringRes
    var rutaTextoBoton1 : Int ?= null
    fun conTextoBoton1(@StringRes rutaString : Int): DialogoGenerico {
        this.rutaTextoBoton1 = rutaString
        return this
    }

    private @StringRes
    var rutaTextoBoton2 : Int ?= null
    fun conTextoBoton2(@StringRes rutaString : Int): DialogoGenerico {
        this.rutaTextoBoton2 = rutaString
        return this
    }

    private var tipoDialogo : TipoDialogo =
        TipoDialogo.OK
    fun conTipoDialogo(tipoDialogo : TipoDialogo = TipoDialogo.OK) : DialogoGenerico {
        this.tipoDialogo = tipoDialogo
        return this
    }

    fun mostrarDialogo(fragmentManager: FragmentManager, etiqueta : String){
        show(fragmentManager,etiqueta)
    }

}
