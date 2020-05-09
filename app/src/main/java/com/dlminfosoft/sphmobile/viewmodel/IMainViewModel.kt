package com.dlminfosoft.sphmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.YearlyRecordResult

interface IMainViewModel {

    fun getListOfData(internetAvailable: Boolean)
    fun yearlyRecordListObservable(): MutableLiveData<YearlyRecordResult>
}