package com.dlminfosoft.sphmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dlminfosoft.sphmobile.model.MainApiResponse
import com.dlminfosoft.sphmobile.repository.RepositoryImpl

/**
 * This class interact with RepositoryImpl and hold observable data
 */
class MainViewModelImpl(private val repoInstance: RepositoryImpl) :
    ViewModel(), IMainViewModel {

    private var mainResponseLiveData = MutableLiveData<MainApiResponse>()

    init {
        fetchDataFromRepo()
    }

    /**
     *  Invoke to repository method fetchDataFromServerOrDb() to get data
     */
    override fun fetchDataFromRepo() {
        mainResponseLiveData = repoInstance.fetchDataFromServerOrDb()
    }

    /**
     *  This method return observable liveData of MainApiResponse
     */
    override fun getObservableMainApiResponse(): MutableLiveData<MainApiResponse> {
        return mainResponseLiveData
    }


}