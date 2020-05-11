package com.dlminfosoft.sphmobile.utility

import android.content.Context
import android.net.ConnectivityManager

class NetManager(private var applicationContext: Context) {

    /**
     * Check and return result of internet connectivity
     */
    val isConnectedToInternet: Boolean
        get() {
            val conManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
            val ni = conManager.activeNetworkInfo
            return ni != null && ni.isConnected
        }
}