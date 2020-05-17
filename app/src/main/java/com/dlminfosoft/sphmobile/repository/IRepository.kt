package com.dlminfosoft.sphmobile.repository

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.MainApiResponse
import com.dlminfosoft.sphmobile.model.YearlyRecord

/**
 * This class implemented by RepositoryImpl class
 */
interface IRepository {
    fun fetchDataFromServerOrDb(): MutableLiveData<MainApiResponse>
    fun insertIntoTable(yearlyRecord: List<YearlyRecord>)
    fun deleteAllRecord()
    fun getAllRecordsFromTable(): List<YearlyRecord>
}