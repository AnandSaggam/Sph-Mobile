package com.dlminfosoft.sphmobile.repository

import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
import java.util.TreeMap
import kotlin.collections.ArrayList

object UseCaseRepository {

    /**
     * Computing yearly records and return list of records
     */
    fun getYearlyRecordResult(response: UsageDataResponse?): YearlyRecordResult {

        val yearlyRecordList = ArrayList<YearlyRecord>()
        response?.let {
            if (it.success && it.result.records.isNotEmpty()) {
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
            } else {
                return YearlyRecordResult(it.success, yearlyRecordList, true)
            }
        } ?: run {
            return YearlyRecordResult(false, yearlyRecordList, true)
        }

        return YearlyRecordResult(response.success, yearlyRecordList, true)
    }

    /**
     * Create and return instance of YearlyRecord
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