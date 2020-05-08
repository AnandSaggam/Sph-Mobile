package com.dlminfosoft.sphmobile.webservice

import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.utility.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface IApiServiceMethods {

    @GET(Constants.METHOD_GET_DATA_USAGE)
    fun getDataUsageDetails(@Query(Constants.FIELD_RESOURCE_ID) resourceId: String = Constants.FIELD_RESOURCE_ID_VALUE): Call<UsageDataResponse>
}