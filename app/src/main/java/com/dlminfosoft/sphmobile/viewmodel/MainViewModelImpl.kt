package com.dlminfosoft.sphmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
import com.dlminfosoft.sphmobile.repository.RepositoryImpl

class MainViewModelImpl(private val repoInstance: RepositoryImpl) :
    ViewModel(), IMainViewModel {

    private var dataUsageResponseYearlyRecord = MutableLiveData<YearlyRecordResult>()

    init {
        getListOfData()
    }

    /*
    *  Invoke to repository method callDataUsageApi() for api call
    */
    override fun getListOfData() {
        dataUsageResponseYearlyRecord = repoInstance.makeCallToGetYearlyRecords()
    }

    /*
    *  This method return observable liveData of YearlyRecordList
    */
    override fun yearlyRecordListObservable(): MutableLiveData<YearlyRecordResult> {
        return dataUsageResponseYearlyRecord
    }
}