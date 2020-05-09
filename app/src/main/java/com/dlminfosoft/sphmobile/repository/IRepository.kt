package com.dlminfosoft.sphmobile.repository

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.YearlyRecordResult

interface IRepository {
    fun makeCallToGetYearlyRecords(internetAvailable: Boolean): MutableLiveData<YearlyRecordResult>
}