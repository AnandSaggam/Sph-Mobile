package com.dlminfosoft.sphmobile.repository

import androidx.lifecycle.MutableLiveData
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.model.YearlyRecordResult

interface IRepository {
    fun makeCallToGetYearlyRecords(): MutableLiveData<YearlyRecordResult>
    fun insertIntoTable(yearlyRecord: List<YearlyRecord>)
    fun deleteAllRecord()
    fun getAllRecordsFromTable(): List<YearlyRecord>
}