package com.dlminfosoft.sphmobile.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dlminfosoft.sphmobile.utility.Constants

@Entity(tableName = Constants.YEARLY_RECORD_TABLE_NAME)
data class YearlyRecordTable(
    @ColumnInfo(name = Constants.COLUMN_NAME_YEAR) val year: String,
    @ColumnInfo(name = Constants.COLUMN_NAME_TOTAL_VOLUME) val totalVolume: Double,
    @ColumnInfo(
        name = Constants.COLUMN_NAME_IS_DECREASE_VOLUME,
        defaultValue = false.toString()
    ) val isDecreaseVolumeData: Boolean = false,
    @ColumnInfo(
        name = Constants.COLUMN_NAME_DECREASE_QUARTER_KEY,
        defaultValue = ""
    ) val decreaseVolumeQuarterKey: String = "",
    @ColumnInfo(name = Constants.COLUMN_NAME_DATA_USAGE) val dataUsageJson: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COLUMN_NAME_ID)
    var id: Int = 0
}