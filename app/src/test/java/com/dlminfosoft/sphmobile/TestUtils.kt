package com.dlminfosoft.sphmobile

import com.dlminfosoft.sphmobile.model.RecordsData
import com.dlminfosoft.sphmobile.model.ResultData
import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.repository.MockInterceptor
import okhttp3.OkHttpClient
import java.util.*
import kotlin.collections.ArrayList

/**
 * This class contains dummy data
 * which used as request and response for other test cases
 */
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

    private fun getRecordsDataQuarterList(): ArrayList<RecordsData> {
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
        val recordsDataList = ResultData(getRecordsDataQuarterList(), 4)
        return UsageDataResponse(true, recordsDataList)
    }

    fun getDummyYearlyRecordResultSuccess(): ArrayList<YearlyRecord> {
        return getDummyYearlyRecordList()
    }

    fun getDummyUsageDataResponseWithEmptyList(): UsageDataResponse {
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

    fun provideOkHttpClient(
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(MockInterceptor())
            .build()
    }

    const val getSuccessResponseBody =
        """{"help": "https://data.gov.sg/api/3/action/help_show?name=datastore_search", "success": true, "result": {"resource_id": "a807b7ab-6cad-4aa6-87d0-e283a7353a0f", "fields": [{"type": "int4", "id": "_id"}, {"type": "text", "id": "quarter"}, {"type": "numeric", "id": "volume_of_mobile_data"}], "records": [{"volume_of_mobile_data": "0.000384", "quarter": "2004-Q3", "_id": 1}, {"volume_of_mobile_data": "0.000543", "quarter": "2004-Q4", "_id": 2}, {"volume_of_mobile_data": "0.00062", "quarter": "2005-Q1", "_id": 3}, {"volume_of_mobile_data": "0.000634", "quarter": "2005-Q2", "_id": 4}, {"volume_of_mobile_data": "0.000718", "quarter": "2005-Q3", "_id": 5}], "_links": {"start": "/api/action/datastore_search?limit=5&resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f", "next": "/api/action/datastore_search?offset=5&limit=5&resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f"}, "limit": 5, "total": 59}}"""

    const val getSuccessResponseBodyWithoutRecord =
        """{ "success": true, "result": {"records": []}}"""
    const val SUCCESS_RESULT = "Success_result"
    const val SUCCESS_RESULT_WITHOUT_RECORD = "Success_result_without_record"
    const val FAILURE_RESULT = "Failure_result"
}