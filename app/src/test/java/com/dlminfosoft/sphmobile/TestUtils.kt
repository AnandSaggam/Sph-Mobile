package com.dlminfosoft.sphmobile

import com.dlminfosoft.sphmobile.model.*
import java.util.*
import kotlin.collections.ArrayList

object TestUtils {

    private fun getDummyYearlyRecord(year: String): YearlyRecord {
        val treeMapData = TreeMap<String, Double>()
        treeMapData["Q1"] = 3.0
        treeMapData["Q2"] = 4.0
        treeMapData["Q3"] = 6.0
        treeMapData["Q4"] = 7.0
        return YearlyRecord(year, treeMapData, 20.00, false, "Q1")
    }

    fun getDummyYearlyRecordList(): ArrayList<YearlyRecord> {
        val yearlyRecordDataList = ArrayList<YearlyRecord>()
        yearlyRecordDataList.add(getDummyYearlyRecord("2012"))
        yearlyRecordDataList.add(getDummyYearlyRecord("2013"))
        yearlyRecordDataList.add(getDummyYearlyRecord("2014"))
        return yearlyRecordDataList
    }
}