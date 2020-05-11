package com.dlminfosoft.sphmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.YearlyRecordResult

/**
 * This interface implemented by MainViewModelImpl
 */
interface IMainViewModel {

    fun getListOfData()
    fun yearlyRecordListObservable(): MutableLiveData<YearlyRecordResult>
}