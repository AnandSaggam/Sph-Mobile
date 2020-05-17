package com.dlminfosoft.sphmobile.repository

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.database.YearlyRecordDao
import com.dlminfosoft.sphmobile.model.MainApiResponse
import com.dlminfosoft.sphmobile.model.UsageDataResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.repository.UseCaseRepository.getYearlyRecordList
import com.dlminfosoft.sphmobile.utility.Constants
import com.dlminfosoft.sphmobile.utility.LocalizationInfoProvider
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
    private val netManager: NetManager,
    private val localizationProvider: LocalizationInfoProvider
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
    override fun fetchDataFromServerOrDb(): MutableLiveData<MainApiResponse> {
        val mainApiResponse =
            MutableLiveData(MainApiResponse(MutableLiveData(), MutableLiveData()))

        if (netManager.isConnectedToInternet) {
            apiServiceInstance.getDataUsageDetails()
                .enqueue(object : Callback<UsageDataResponse> {
                    override fun onResponse(
                        call: Call<UsageDataResponse>,
                        response: Response<UsageDataResponse>
                    ) {
                        if (response.code() == Constants.STATUS_CODE_SUCCESS && response.body() != null) {
                            val recordList: List<YearlyRecord> =
                                getYearlyRecordList(response.body()!!)

                            if (recordList.isNotEmpty()) {
                                mainApiResponse.value?.yearlyRecordListLiveData?.value =
                                    recordList
                                launch {
                                    withContext(Dispatchers.Default) {
                                        deleteAllRecord()
                                    }
                                    insertIntoTable(recordList)
                                }
                            } else {
                                mainApiResponse.value?.errorLiveData?.value =
                                    Error(localizationProvider.getEmptyListMessage())
                            }
                        } else {
                            Timber.d("onResponse: Something went wrong")
                            mainApiResponse.value?.errorLiveData?.value =
                                Error(localizationProvider.getSomethingWrongMessage())
                        }
                    }

                    override fun onFailure(
                        call: Call<UsageDataResponse>,
                        t: Throwable
                    ) {
                        Timber.d("OnFailure: ${t.message.toString()}")
                        val errorLiveData = MutableLiveData<Error>()
                        errorLiveData.value = Error(t.message.toString())
                        mainApiResponse.value?.errorLiveData?.value =
                            Error(t.message.toString())
                    }
                })
        } else {
            // Launching background thread using coroutines
            launch {
                // Fetch data from local database
                val recordList = getAllRecordsFromTable()
                withContext(Dispatchers.Main) {
                    if (recordList.isNotEmpty()) {
                        Timber.d("Display data from local database: ${recordList.size}")
                        mainApiResponse.value?.yearlyRecordListLiveData?.value = recordList
                    } else {
                        Timber.d("Internet connection not available")
                        mainApiResponse.value?.errorLiveData?.value =
                            Error(localizationProvider.getNoInternetMessage())
                    }
                }
            }
        }
        return mainApiResponse
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}