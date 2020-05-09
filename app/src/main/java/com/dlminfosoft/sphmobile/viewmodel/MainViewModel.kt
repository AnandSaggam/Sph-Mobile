package com.dlminfosoft.sphmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
import com.dlminfosoft.sphmobile.repository.Repository

class MainViewModel(private val repoInstance: Repository) :
    ViewModel(), IMainViewModel {

    private var dataUsageResponseYearlyRecord = MutableLiveData<YearlyRecordResult>()

    /*
    *  Invoke to repository method callDataUsageApi() for api call
    */
    override fun getListOfData(internetAvailable: Boolean) {
        dataUsageResponseYearlyRecord = repoInstance.makeCallToGetYearlyRecords(internetAvailable)
    }

    /*
    *  This method return observable liveData of YearlyRecordList
    */
    override fun yearlyRecordListObservable(): MutableLiveData<YearlyRecordResult> {
        return dataUsageResponseYearlyRecord
    }
}