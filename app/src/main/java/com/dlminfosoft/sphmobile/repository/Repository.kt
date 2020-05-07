package com.dlminfosoft.sphmobile.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.database.YearlyRecordDao
import com.dlminfosoft.sphmobile.database.YearlyRecordTable
import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
import com.dlminfosoft.sphmobile.webservice.IApiServiceMethods
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.TreeMap
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


object Repository : CoroutineScope {

    private lateinit var yearlyRecordDao: YearlyRecordDao

    fun initDao(dao: YearlyRecordDao) {
        yearlyRecordDao = dao
    }

    private suspend fun insert(yearlyRecord: ArrayList<YearlyRecordTable>) {
        withContext(Dispatchers.IO) {
            yearlyRecordDao.insertAll(yearlyRecord)
        }
    }

    private suspend fun deleteAllRecord() {
        withContext(Dispatchers.IO) {
            yearlyRecordDao.deleteAllRecords()
        }
    }

    private suspend fun getAllUserList(): List<YearlyRecordTable> {
        return yearlyRecordDao.getAllRecordsList()
    }

    /*
    * Make network call to fetch data
    */
    fun getYearlyRecords(internetAvailable: Boolean): MutableLiveData<YearlyRecordResult> {
        val responseLiveData = MutableLiveData<YearlyRecordResult>()

        if (internetAvailable) {
            IApiServiceMethods.createRetrofit().getDataUsageDetails()
                .enqueue(object : Callback<UsageDataResponse> {
                    override fun onResponse(
                        call: Call<UsageDataResponse>,
                        response: Response<UsageDataResponse>
                    ) {
                        val resultRecord: YearlyRecordResult =
                            getResultYearlyRecord(response.body())
                        responseLiveData.value = resultRecord

                        launch {
                            deleteAllRecord()
                        }
                        launch {
                            if (resultRecord.recordList.isNotEmpty()) {
                                insert(preparedDataForInsert(resultRecord.recordList))
                            }
                        }
                    }

                    override fun onFailure(call: Call<UsageDataResponse>, t: Throwable) {
                        responseLiveData.value = null
                    }
                })
        } else {
            launch {
                val result = getAllUserList()
                withContext(Dispatchers.Main) {
                    responseLiveData.value = getYearlyRecordList(result)
                    Log.e("Record from db", "==>${getYearlyRecordList(result).recordList.size}")
                }
            }
        }
        return responseLiveData
    }

    private fun getYearlyRecordList(list: List<YearlyRecordTable>?): YearlyRecordResult {
        val listOfData = ArrayList<YearlyRecord>()
        val type: Type = object : TypeToken<TreeMap<String, Double>>() {}.type
        list?.let {
            for (item in it) {

                val treeMapDataUsage: TreeMap<String, Double> =
                    Gson().fromJson(item.dataUsageJson, type)
                val record = getYearlyRecord(
                    item.year,
                    treeMapDataUsage,
                    item.totalVolume,
                    item.isDecreaseVolumeData,
                    item.decreaseVolumeQuarterKey
                )
                listOfData.add(record)
            }
        }
        return YearlyRecordResult(true, listOfData)
    }


    private fun preparedDataForInsert(recordList: ArrayList<YearlyRecord>): ArrayList<YearlyRecordTable> {
        val yearlyRecordTableList = ArrayList<YearlyRecordTable>()
        for (item in recordList) {
            val jsonDataUsage = Gson().toJson(item.treeMapWithDataUsage)
            val data = YearlyRecordTable(
                item.year,
                item.totalVolume,
                item.isDecreaseVolumeData,
                item.decreaseVolumeQuarterKey,
                jsonDataUsage
            )
            yearlyRecordTableList.add(data)
        }
        return yearlyRecordTableList
    }

    /*
    * Computing yearly records and return list of records
    */
    private fun getResultYearlyRecord(response: UsageDataResponse?): YearlyRecordResult {
        val yearlyRecordList = ArrayList<YearlyRecord>()
        try {
            response?.let {
                if (response.success && response.result.records.isNotEmpty()) {
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}