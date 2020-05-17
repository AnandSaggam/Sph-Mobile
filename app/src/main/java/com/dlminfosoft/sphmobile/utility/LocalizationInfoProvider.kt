package com.dlminfosoft.sphmobile.utility

import android.content.Context
import com.dlminfosoft.sphmobile.R

/**
 * This class provide string messages
 */
class LocalizationInfoProvider(private val context: Context) {

    fun getNoInternetMessage(): String {
        return context.resources.getString(R.string.no_internet)
    }

    fun getEmptyListMessage(): String {
        return context.resources.getString(R.string.no_record_available)
    }

    fun getSomethingWrongMessage(): String {
        return context.resources.getString(R.string.something_went_wrong)
    }
}