package com.dlminfosoft.sphmobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dlminfosoft.sphmobile.utility.Constants
import java.util.TreeMap
import kotlin.collections.ArrayList

/*
* This class hold list of each year data and api result
*/
data class YearlyRecordResult(
    val isSuccess: Boolean,
    val recordList: ArrayList<YearlyRecord>
)

/*
* This class hold value of each year data
*/
@Entity(tableName = Constants.YEARLY_RECORD_TABLE_NAME)
data class YearlyRecord(
    val year: String,
    val treeMapWithDataUsage: TreeMap<String, Double>,
    val totalVolume: Double,
    val isDecreaseVolumeData: Boolean = false,
    val decreaseVolumeQuarterKey: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}