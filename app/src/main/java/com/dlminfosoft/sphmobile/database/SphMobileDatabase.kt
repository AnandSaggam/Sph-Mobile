package com.dlminfosoft.sphmobile.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dlminfosoft.sphmobile.utility.Constants

@Database(entities = [YearlyRecordTable::class], version = 1)
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