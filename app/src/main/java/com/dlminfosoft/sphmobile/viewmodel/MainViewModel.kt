package com.dlminfosoft.sphmobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.database.SphMobileDatabase
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
import com.dlminfosoft.sphmobile.repository.Repository

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private var dataUsageResponseYearlyRecord = MutableLiveData<YearlyRecordResult>()

    init {
        Repository.initDao(SphMobileDatabase.getDatabase(app).getYearlyRecordDao())
    }

    /*
    *  Invoke to repository method callDataUsageApi() for api call
    */
    fun callDataUsageDetails(internetAvailable: Boolean) {
        dataUsageResponseYearlyRecord = Repository.getYearlyRecords(internetAvailable)
    }

    /*
    *  This method return observable liveData of YearlyRecordList
    */
    fun yearlyRecordListObservable(): MutableLiveData<YearlyRecordResult> {
        return dataUsageResponseYearlyRecord
    }
}