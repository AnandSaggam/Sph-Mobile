package com.dlminfosoft.sphmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.MainApiResponse

/**
 * This interface implemented by MainViewModelImpl
 */
interface IMainViewModel {

    fun fetchDataFromRepo()
    fun getObservableMainApiResponse(): MutableLiveData<MainApiResponse>
}