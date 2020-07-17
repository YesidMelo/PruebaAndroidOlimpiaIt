package com.mitiempo.pruebaolimpiaitandroid.Modelos

import com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley.PreferenciasServidor
import com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley.ProxyVolley

interface ModeloBase :
    PreferenciasServidor.PreferenciasServidorParcelable,
    ProxyVolley.VolleyParcelable
{

}