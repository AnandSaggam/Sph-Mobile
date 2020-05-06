package com.dlminfosoft.sphmobile.webservice

import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.utility.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface IApiServiceMethods {


    @GET(Constants.METHOD_GET_DATA_USAGE)
    fun getDataUsageDetails(@Query(Constants.FIELD_RESOURCE_ID) resourceId: String = Constants.FIELD_RESOURCE_ID_VALUE): Call<UsageDataResponse>


    /*
    * Create retrofit singleton instance
    */
    companion object Factory {
        fun createRetrofit(): IApiServiceMethods {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()
            return retrofit.create<IApiServiceMethods>(IApiServiceMethods::class.java)
        }
    }
}