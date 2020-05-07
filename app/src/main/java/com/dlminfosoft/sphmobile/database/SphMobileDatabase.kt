package com.dlminfosoft.sphmobile.database

import android.content.Context
import androidx.room.*
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.utility.Constants

@Database(entities = [YearlyRecord::class], version = 1)
@TypeConverters(DbConverters::class)
abstract class SphMobileDatabase : RoomDatabase() {

    abstract fun getYearlyRecordDao(): YearlyRecordDao

    companion object {

        // Create singleton instances of database
        @Volatile
        private var INSTANCE: SphMobileDatabase? = null

        fun getDatabase(context: Context): SphMobileDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // At a time only one thread can execute this code of block
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SphMobileDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}