package com.dlminfosoft.sphmobile.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.utility.Constants

@Dao
interface YearlyRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertAll(list: List<YearlyRecord>):Array<Long>

    @Query("DELETE FROM ${Constants.YEARLY_RECORD_TABLE_NAME}")
    suspend fun deleteAllRecords():Int

    @Query("SELECT * FROM ${Constants.YEARLY_RECORD_TABLE_NAME}")
    suspend fun getAllRecordsList(): List<YearlyRecord>
}