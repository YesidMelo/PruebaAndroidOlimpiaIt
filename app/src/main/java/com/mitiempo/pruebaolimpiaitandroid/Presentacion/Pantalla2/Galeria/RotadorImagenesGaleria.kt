package com.mitiempo.pruebaolimpiaitandroid.Presentacion.Pantalla2.Galeria

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface

class RotadorImagenesGaleria {

    private val T = "RotadorImagenes"

    private var contexto : Context ?= null
    fun conContext(contexto : Context) : RotadorImagenesGaleria {
        this.contexto = contexto
        return this
    }

    private var rutaFoto : String ?= null
    fun conRutaImagenCamara(ruta : String) : RotadorImagenesGaleria {
        this.rutaFoto = ruta
        return this
    }

    private var ancho : Int = 100
    fun conAncho(ancho : Int) : RotadorImagenesGaleria {
        this.ancho = ancho
        return this
    }

    private var alto : Int = 100
    fun conAlto(alto : Int) : RotadorImagenesGaleria {
        this.alto = alto
        return this
    }


    private var escuchadorImagenRotada : ((Bitmap)->Unit)?= null
    fun conEscuchadorImagenRotada(escuchadorImagenRotada : ((Bitmap)->Unit)): RotadorImagenesGaleria {
        this.escuchadorImagenRotada = escuchadorImagenRotada
        return this
    }

    fun rotarImagen(){

        val rotacionImagen = capturarRotacionImagen()
        val funcionRotadora = seleccionarRotacion(rotacionImagen)
        funcionRotadora.invoke(rotacionImagen)

    }

    private fun capturarRotacionImagen() : Int{

        val inputStream = contexto!!.contentResolver.openInputStream(Uri.parse(rutaFoto))!!
        var rotacion = 0
        try {
            val exsifInterface = ExifInterface(inputStream)
            val orientacion = exsifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)
            when (orientacion) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotacion = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotacion = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotacion = 90
            }
        }catch (e : Exception){
            Log.e(T,"",e)
        }
        return rotacion
    }

    private fun seleccionarRotacion(rotacionImagen : Int) : (Int)->Unit{
        return when(rotacionImagen){
            0 -> ::noRotar
            else -> ::rotar90Grados
        }
    }

    private fun  noRotar(angulo: Int){
        val inputStream = contexto!!.contentResolver.openInputStream(Uri.parse(rutaFoto))
        val imagen = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        try {
            escuchadorImagenRotada?.invoke(imagen)
        } catch (e: Exception) {
        }
    }

    private fun rotar90Grados(angulo :Int){
        val inputStream = contexto!!.contentResolver.openInputStream(Uri.parse(rutaFoto))
        val imageARotar = BitmapFactory.decodeStream(inputStream)

        inputStream?.close()

        val matriz = Matrix()
        matriz.postRotate(angulo.toFloat())

        val imagenRotada = Bitmap.createBitmap(imageARotar,0,0,imageARotar.width,imageARotar.height,matriz,true)
        escuchadorImagenRotada?.invoke(imagenRotada)

    }

}