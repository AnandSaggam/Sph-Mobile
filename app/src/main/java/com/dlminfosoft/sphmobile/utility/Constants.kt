package com.dlminfosoft.sphmobile.utility

import android.content.Context
import android.net.ConnectivityManager


/*
*  This object contains all the constant field and methods
*/
object Constants {

    // Api constants
    const val METHOD_GET_DATA_USAGE = "datastore_search"
    const val FIELD_RESOURCE_ID = "resource_id"
    const val FIELD_RESOURCE_ID_VALUE = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"

    // Database constants
    const val DATABASE_NAME = "SphMobileDatabase"
    const val YEARLY_RECORD_TABLE_NAME = "YearlyRecords"

    /*
    * Check and return result of internet connectivity
    */
    fun isInternetAvailable(mContext: Context): Boolean {
        val conManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val networkInfo = conManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}