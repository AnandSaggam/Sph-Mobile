package com.dlminfosoft.sphmobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.repository.Repository
import java.util.TreeMap
import kotlin.collections.ArrayList

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private var dataUsageResponse = MutableLiveData<UsageDataResponse>()
    /*
    *  Invoke to repository method callDataUsageApi() for api call
    */
    fun callDataUsageDetails() {
        dataUsageResponse = Repository.callDataUsageApi()
    }

    /*
    * Observable for callDataUsageApi() response
    */
    fun getListDataObservable(): MutableLiveData<UsageDataResponse> {
        return dataUsageResponse
    }

    /*
    * Computing yearly records and return list of records
    */
    fun getResultYearlyRecord(): ArrayList<YearlyRecord> {
        val yearlyRecordList = ArrayList<YearlyRecord>()
        try {
            dataUsageResponse.value?.let {
                var currentYear = "0000"
                var mapWithDataUsage = TreeMap<String, Float>()
                var totalVolume = 0f

                for (item in dataUsageResponse.value!!.result.records) {
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
                            totalVolume = 0f
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
        return yearlyRecordList
    }

    /*
    * Return instance of YearlyRecord
    */
    private fun getYearlyRecord(
        currentYear: String,
        mapWithDataUsage: TreeMap<String, Float>,
        totalVolume: Float,
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