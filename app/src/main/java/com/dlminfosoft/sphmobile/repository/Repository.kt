package com.dlminfosoft.sphmobile.repository

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
import com.dlminfosoft.sphmobile.webservice.IApiServiceMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.TreeMap
import kotlin.collections.ArrayList

object Repository {

    /*
    * Make network call to fetch data
    */
    fun getYearlyRecords(): MutableLiveData<YearlyRecordResult> {
        val responseLiveData = MutableLiveData<YearlyRecordResult>()

        IApiServiceMethods.createRetrofit().getDataUsageDetails()
            .enqueue(object : Callback<UsageDataResponse> {
                override fun onResponse(
                    call: Call<UsageDataResponse>,
                    response: Response<UsageDataResponse>
                ) {
                    responseLiveData.value = getResultYearlyRecord(response.body())
                }

                override fun onFailure(call: Call<UsageDataResponse>, t: Throwable) {
                    responseLiveData.value = null
                }
            })
        return responseLiveData
    }

    /*
    * Computing yearly records and return list of records
    */
    private fun getResultYearlyRecord(body: UsageDataResponse?): YearlyRecordResult {
        val yearlyRecordList = ArrayList<YearlyRecord>()
        try {
            body?.let {

                var currentYear = "0000"
                var mapWithDataUsage = TreeMap<String, Double>()
                var totalVolume = 0.0

                for (item in it.result.records) {
                    val yearWithQuarter = item.quarter.split("-")
                    if (currentYear == "0000") {
                        // First record of list
                        mapWithDataUsage = TreeMap()
                        currentYear = yearWithQuarter[0]
                        mapWithDataUsage[yearWithQuarter[1]] = item.volume_of_mobile_data
                        totalVolume += item.volume_of_mobile_data
                    } else {
                        // Record with same year
                        if (yearWithQuarter[0] == (currentYear)) {
                            mapWithDataUsage[yearWithQuarter[1]] = item.volume_of_mobile_data
                            totalVolume += item.volume_of_mobile_data
                        } else {
                            // Record with new year, so add old year data in list
                            yearlyRecordList.add(
                                getYearlyRecord(
                                    currentYear,
                                    mapWithDataUsage,
                                    totalVolume,
                                    mapWithDataUsage.minBy { it.value }?.key != mapWithDataUsage.firstKey(),
                                    mapWithDataUsage.minBy { it.value }?.key.toString()
                                )
                            )
                            // Resetting data
                            mapWithDataUsage = TreeMap()
                            totalVolume = 0.0
                            totalVolume += item.volume_of_mobile_data
                            currentYear = yearWithQuarter[0]
                            mapWithDataUsage[yearWithQuarter[1]] = item.volume_of_mobile_data
                        }
                    }
                }

                // Add last record in list
                val yearlyRecord =
                    getYearlyRecord(
                        currentYear,
                        mapWithDataUsage,
                        totalVolume,
                        mapWithDataUsage.minBy { it.value }?.key != mapWithDataUsage.firstKey(),
                        mapWithDataUsage.minBy { it.value }?.key.toString()
                    )
                yearlyRecordList.add(yearlyRecord)
            }
        } catch (e: Exception) {
        }
        return YearlyRecordResult(true, yearlyRecordList)
    }

    /*
    * Return instance of YearlyRecord
    */
    private fun getYearlyRecord(
        currentYear: String,
        mapWithDataUsage: TreeMap<String, Double>,
        totalVolume: Double,
        isDecreaseVolumeData: Boolean = false,
        decreaseVolumeQuarterKey: String = ""
    ) =
        YearlyRecord(
            currentYear,
            mapWithDataUsage,
            totalVolume,
            isDecreaseVolumeData,
            decreaseVolumeQuarterKey
        )
}