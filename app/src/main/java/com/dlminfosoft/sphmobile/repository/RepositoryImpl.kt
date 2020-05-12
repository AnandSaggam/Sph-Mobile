package com.dlminfosoft.sphmobile.repository

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.database.YearlyRecordDao
import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
import com.dlminfosoft.sphmobile.repository.UseCaseRepository.getYearlyRecordResult
import com.dlminfosoft.sphmobile.utility.NetManager
import com.dlminfosoft.sphmobile.webservice.IApiServiceMethods
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * This class handle all the network operation and database CRUD operations
 */
class RepositoryImpl(
    private val yearlyRecordDao: YearlyRecordDao,
    private val apiServiceInstance: IApiServiceMethods,
    private val netManager: NetManager
) : CoroutineScope, IRepository {

    /**
     * Insert list of records in YearlyRecords table
     */
    override fun insertIntoTable(yearlyRecordDataList: List<YearlyRecord>) {
        // Do insertion in background thread using coroutine
        if (yearlyRecordDataList.isNotEmpty()) {
            launch {
                withContext(Dispatchers.IO) {
                    val result = yearlyRecordDao.insertAll(yearlyRecordDataList)
                    Timber.d("Number of record inserted result: ${result.size}")
                }
            }
        }
    }

    /**
     * Delete all records from YearlyRecords table
     */
    override fun deleteAllRecord() {
        runBlocking {
            val result = yearlyRecordDao.deleteAllRecords()
            Timber.d("Number of record deleted result: $result")
        }
    }

    /**
     * Retrieving list of records YearlyRecords table
     */
    override fun getAllRecordsFromTable(): List<YearlyRecord> {
        var resultFinal: List<YearlyRecord> = ArrayList()
        runBlocking {
            Dispatchers.IO {
                resultFinal = yearlyRecordDao.getAllRecordsList()
            }
        }
        return resultFinal
    }

    /**
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
                            withContext(Dispatchers.Default) {
                                deleteAllRecord()
                            }
                            insertIntoTable(recordResult.recordList)
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}