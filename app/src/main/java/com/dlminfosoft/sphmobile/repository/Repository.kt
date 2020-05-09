package com.dlminfosoft.sphmobile.repository

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.database.YearlyRecordDao
import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
import com.dlminfosoft.sphmobile.utility.NetManager
import com.dlminfosoft.sphmobile.webservice.IApiServiceMethods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.TreeMap
import java.util.logging.Logger
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

/*
* This class handle all the network operation and database CRUD operations
*/
class Repository(
    private val yearlyRecordDao: YearlyRecordDao,
    private val apiServiceInstance: IApiServiceMethods,
    private val netManager: NetManager
) : CoroutineScope, IRepository {
    private val logger = Logger.getLogger("com.dlminfosoft.sphmobile")

    /*
    * Insert list of records in YearlyRecords table
    */
    private suspend fun insertIntoTable(yearlyRecord: List<YearlyRecord>) {
        // Do insertion in background thread using coroutine
        withContext(Dispatchers.IO) {
            deleteAllRecord()
            val result = yearlyRecordDao.insertAll(yearlyRecord)
            logger.info("Number of record inserted result: ${result.size}")
        }
    }

    /*
    * Delete all records from YearlyRecords table
    */
    private suspend fun deleteAllRecord() {
        withContext(Dispatchers.IO) {
            val result = yearlyRecordDao.deleteAllRecords()
            logger.info("Number of record deleted result: $result")
        }
    }

    /*
    * Retrieving list of records YearlyRecords table
    */
    private suspend fun getAllRecordsFromTable(): List<YearlyRecord> {
        return yearlyRecordDao.getAllRecordsList()
    }

    /*
    * If network available fetch data from server else fetch from local database
    */
    override fun makeCallToGetYearlyRecords(): MutableLiveData<YearlyRecordResult> {
        val responseLiveData = MutableLiveData<YearlyRecordResult>()
        if (netManager.isConnectedToInternet) {
            apiServiceInstance.getDataUsageDetails()
                .enqueue(object : Callback<UsageDataResponse> {
                    override fun onResponse(
                        call: Call<UsageDataResponse>,
                        response: Response<UsageDataResponse>
                    ) {
                        val recordResult: YearlyRecordResult =
                            getYearlyRecordResult(response.body())
                        responseLiveData.value = recordResult

                        launch {
                            if (recordResult.recordList.isNotEmpty()) {
                                insertIntoTable(recordResult.recordList)
                            }
                        }
                    }

                    override fun onFailure(call: Call<UsageDataResponse>, t: Throwable) {
                        responseLiveData.value = null
                    }
                })
        } else {
            // Launching background thread using coroutines
            launch {
                val result = getAllRecordsFromTable()
                withContext(Dispatchers.Main) {
                    responseLiveData.value =
                        YearlyRecordResult(true, result, false)
                }
            }
        }
        return responseLiveData
    }

    /*
    * Computing yearly records and return list of records
    */
    private fun getYearlyRecordResult(response: UsageDataResponse?): YearlyRecordResult {

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

    /*
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}