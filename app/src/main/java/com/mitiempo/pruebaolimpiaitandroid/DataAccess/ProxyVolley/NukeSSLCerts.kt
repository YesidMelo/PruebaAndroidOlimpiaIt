package com.mitiempo.pruebaolimpiaitandroid.DataAccess.ProxyVolley

import android.util.Log
import java.security.SecureRandom
import javax.net.ssl.*
import javax.security.cert.X509Certificate


class NukeSSLCerts {
    companion object {

        fun nuke() {
            try {

                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun checkClientTrusted(
                        p0: Array<out java.security.cert.X509Certificate>?,
                        p1: String?
                    ) {
                        Log.e("Certificate", "checkClientTrusted")
                    }

                    override fun checkServerTrusted(
                        p0: Array<out java.security.cert.X509Certificate>?,
                        p1: String?
                    ) {
                        Log.e("Certificate", "checkServerTrusted")
                    }

                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                        Log.e("Certificate", "getAcceptedIssuers")
                        val trusttedAnchor = emptyList<X509Certificate>()
                        return trusttedAnchor as Array<java.security.cert.X509Certificate>
                    }


                })

                val sc = SSLContext.getInstance("SSL")
                sc.init(null, trustAllCerts, SecureRandom())
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
                HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
                    override fun verify(p0: String?, p1: SSLSession?): Boolean {
                        return true
                    }

                })
            } catch (e: Exception) {
                Log.e("certificate", "error")
            }
        }

    }
}