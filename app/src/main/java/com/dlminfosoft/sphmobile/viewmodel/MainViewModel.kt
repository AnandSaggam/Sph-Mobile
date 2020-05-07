package com.dlminfosoft.sphmobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
import com.dlminfosoft.sphmobile.repository.Repository

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private var dataUsageResponseYearlyRecord = MutableLiveData<YearlyRecordResult>()

    /*
    *  Invoke to repository method callDataUsageApi() for api call
    */
    fun callDataUsageDetails() {
        dataUsageResponseYearlyRecord = Repository.getYearlyRecords()
    }

    fun yearlyRecordListObservable(): MutableLiveData<YearlyRecordResult> {
        return dataUsageResponseYearlyRecord
    }
}