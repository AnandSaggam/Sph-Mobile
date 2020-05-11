package com.dlminfosoft.sphmobile

import com.dlminfosoft.sphmobile.model.*
import java.util.*
import kotlin.collections.ArrayList

object TestUtils {

    private fun getDummyYearlyRecord(year: String): YearlyRecord {
        val treeMapData = TreeMap<String, Double>()
        treeMapData["Q1"] = 3.0
        treeMapData["Q2"] = 4.0
        treeMapData["Q3"] = 6.0
        treeMapData["Q4"] = 7.0
        return YearlyRecord(year, treeMapData, 20.00, false, "Q1")
    }

    fun getDummyYearlyRecordList(): ArrayList<YearlyRecord> {
        val yearlyRecordDataList = ArrayList<YearlyRecord>()
        yearlyRecordDataList.add(getDummyYearlyRecord("2012"))
        yearlyRecordDataList.add(getDummyYearlyRecord("2013"))
        yearlyRecordDataList.add(getDummyYearlyRecord("2014"))
        return yearlyRecordDataList
    }

    private fun getRecordsDataList(): ArrayList<RecordsData> {
        val recordList = ArrayList<RecordsData>()
        recordList.add(RecordsData(1, "2012-Q1", 3.0))
        recordList.add(RecordsData(2, "2012-Q2", 4.0))
        recordList.add(RecordsData(3, "2012-Q3", 6.0))
        recordList.add(RecordsData(4, "2012-Q4", 7.0))
        return recordList
    }

    fun getDummyEmptyYearlyRecordList(): ArrayList<YearlyRecord> {
        return ArrayList()
    }


    fun getDummyUsageDataResponse(): UsageDataResponse {
        val recordsDataList = ResultData(getRecordsDataList(), 4)
        return UsageDataResponse(true, recordsDataList)
    }

    fun getDummyYearlyRecordResultSuccess(): YearlyRecordResult {
        return YearlyRecordResult(true, getDummyYearlyRecordList(), true)
    }

    fun getDummyUsageDataResponseFailureCase(): UsageDataResponse {
        return UsageDataResponse(false, ResultData(ArrayList(), 0))
    }

    fun getDummyUsageDataResponseDecreaseQuarterVolume(): UsageDataResponse {
        val recordDataList = ArrayList<RecordsData>()
        recordDataList.add(RecordsData(1, "2015-Q1", 3.0))
        recordDataList.add(RecordsData(2, "2015-Q2", 6.0))
        recordDataList.add(RecordsData(3, "2015-Q3", 2.0))
        recordDataList.add(RecordsData(4, "2015-Q4", 7.0))
        val recordsData = ResultData(recordDataList, 4)
        return UsageDataResponse(true, recordsData)
    }
}