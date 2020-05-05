package com.dlminfosoft.sphmobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.repository.Repository
import com.dlminfosoft.sphmobile.utility.Constants

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private var dataUsageResponse = MutableLiveData<UsageDataResponse>()
    fun callDataUsageDetails() {
        Constants.isInternetAvailable(app)
        dataUsageResponse = Repository.callDataUsageApi()
    }

    fun getListDataObservable(): MutableLiveData<UsageDataResponse> {
        return dataUsageResponse
    }

}