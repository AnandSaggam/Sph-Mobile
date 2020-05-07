package com.dlminfosoft.sphmobile.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/*
*  This object contains all the constant field and methods
*/
object Constants {

    // Api constants
    const val BASE_URL = "https://data.gov.sg/api/action/"
    const val METHOD_GET_DATA_USAGE = "datastore_search"
    const val FIELD_RESOURCE_ID = "resource_id"
    const val FIELD_RESOURCE_ID_VALUE = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"

    // Database constants
    const val DATABASE_NAME = "SphMobileDatabase"
    const val YEARLY_RECORD_TABLE_NAME = "YearlyRecords"
    const val COLUMN_NAME_ID = "_Id"
    const val COLUMN_NAME_YEAR = "Year"
    const val COLUMN_NAME_TOTAL_VOLUME = "TotalVolume"
    const val COLUMN_NAME_IS_DECREASE_VOLUME = "DecreaseVolume"
    const val COLUMN_NAME_DECREASE_QUARTER_KEY = "DecreaseQuarterKey"
    const val COLUMN_NAME_DATA_USAGE = "DataUsage"


    /*
    * Check and return result of internet connectivity
    */
    fun isInternetAvailable(mContext: Context): Boolean {
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            if (network != null) {
                val nc =
                    connectivityManager.getNetworkCapabilities(network)
                        ?: return false
                return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                val networkInfo =
                    connectivityManager.allNetworkInfo
                for (tempNetworkInfo in networkInfo) {
                    if (tempNetworkInfo.isConnected) {
                        return true
                    }
                }
            }
        }
        return false
    }
}