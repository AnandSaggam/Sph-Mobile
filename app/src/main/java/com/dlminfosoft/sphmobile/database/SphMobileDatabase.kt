package com.dlminfosoft.sphmobile.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dlminfosoft.sphmobile.model.YearlyRecord

/**
 * This class used for creating app database
 * which contains tables and version name
 */
@Database(entities = [YearlyRecord::class], version = 1)
@TypeConverters(DbConverters::class)
abstract class SphMobileDatabase : RoomDatabase() {

    /**
     * This is instance of Dao interface, auto generated by room database
     */
    abstract fun getYearlyRecordDao(): YearlyRecordDao
}