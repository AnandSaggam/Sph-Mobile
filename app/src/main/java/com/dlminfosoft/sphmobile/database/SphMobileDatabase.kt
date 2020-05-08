package com.dlminfosoft.sphmobile.database

import androidx.room.*
import com.dlminfosoft.sphmobile.model.YearlyRecord

@Database(entities = [YearlyRecord::class], version = 1)
@TypeConverters(DbConverters::class)
abstract class SphMobileDatabase : RoomDatabase() {

    abstract fun getYearlyRecordDao(): YearlyRecordDao
}