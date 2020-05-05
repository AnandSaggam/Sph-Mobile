package com.dlminfosoft.sphmobile.repository

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.webservice.IApiServiceMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object Repository {

    fun callDataUsageApi(): MutableLiveData<UsageDataResponse> {
        val responseLiveData = MutableLiveData<UsageDataResponse>()

        IApiServiceMethods.createRetrofit().getDataUsageDetails()
            .enqueue(object : Callback<UsageDataResponse> {
                override fun onResponse(
                    call: Call<UsageDataResponse>,
                    response: Response<UsageDataResponse>
                ) {
                    responseLiveData.value = response.body()
                }

                override fun onFailure(call: Call<UsageDataResponse>, t: Throwable) {
                    responseLiveData.value = null
                }
            })
        return responseLiveData
    }
}