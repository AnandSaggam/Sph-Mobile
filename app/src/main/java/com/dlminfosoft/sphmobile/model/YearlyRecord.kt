package com.dlminfosoft.sphmobile.model

import java.util.TreeMap

data class YearlyRecord(
    val year: String,
    val hashMapWithDataUsage: TreeMap<String, Float>,
    val totalVolume: Float,
    val isDecreaseVolumeData: Boolean = false,
    val decreaseVolumeQuarterKey: String = ""
)

