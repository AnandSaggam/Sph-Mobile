package com.dlminfosoft.sphmobile.model

import androidx.lifecycle.MutableLiveData

/**
 * This class hold liveData for list of YearlyRecord and Error message
 */
data class MainApiResponse(
    val yearlyRecordListLiveData: MutableLiveData<List<YearlyRecord>>? = null,
    val errorLiveData: MutableLiveData<Error>? = null
)