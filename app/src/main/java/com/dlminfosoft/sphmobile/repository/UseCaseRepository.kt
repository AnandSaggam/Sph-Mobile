package com.dlminfosoft.sphmobile.repository

import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord
import java.util.*
import kotlin.collections.ArrayList

object UseCaseRepository {

    /**
     * Computing yearly records and return list of records
     */
    fun getYearlyRecordList(body: UsageDataResponse): List<YearlyRecord> {
        val yearlyRecordList = ArrayList<YearlyRecord>()

        if (body.success && body.result.records.isNotEmpty()) {
            var currentYear = "0000"
            var mapWithDataUsage = TreeMap<String, Double>()
            var totalVolume = 0.0

            for (item in body.result.records) {
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
                        val decreaseVolumeKey: String =
                            returnDecreaseKeyEntry(mapWithDataUsage)
                        yearlyRecordList.add(
                            YearlyRecord(
                                currentYear,
                                mapWithDataUsage,
                                totalVolume,
                                decreaseVolumeKey != mapWithDataUsage.firstKey(),
                                decreaseVolumeKey
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
            val decreaseVolumeKey: String =
                returnDecreaseKeyEntry(mapWithDataUsage)
            val yearlyRecord =
                YearlyRecord(
                    currentYear,
                    mapWithDataUsage,
                    totalVolume,
                    decreaseVolumeKey != mapWithDataUsage.firstKey(),
                    decreaseVolumeKey
                )
            yearlyRecordList.add(yearlyRecord)
        }

        /**
         * If not have to apply filter then "return yearlyRecordList"
         */
        return getDataBetweenSpecificYear(yearlyRecordList)
    }

    /**
     * Here we are filtering records based on specific year rang
     * comment out below code block to get record for each year
     */
    private fun getDataBetweenSpecificYear(yearlyRecordList: ArrayList<YearlyRecord>): List<YearlyRecord> {
        val specificYearlyRecordList = ArrayList<YearlyRecord>()
        for (item in yearlyRecordList) {
            if (item.year.toInt() in 2008..2018) {
                specificYearlyRecordList.add(item)
            }
        }
        return specificYearlyRecordList
    }

    /**
     * This method return quarter if decrease in volume
     */
    private fun returnDecreaseKeyEntry(mapWithDataUsage: TreeMap<String, Double>): String {
        var previousItemKey = mapWithDataUsage.firstKey()
        val foundDecreaseKey = mapWithDataUsage.firstKey()
        for ((key, currentValue) in mapWithDataUsage.entries) {
            if (currentValue < mapWithDataUsage[previousItemKey] ?: 0.0) {
                return key
            }
            previousItemKey = key
        }
        return foundDecreaseKey
    }
}