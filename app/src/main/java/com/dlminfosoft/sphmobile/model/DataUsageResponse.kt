package com.dlminfosoft.sphmobile.model

import com.google.gson.annotations.SerializedName

/**
 * This class hold response of getUserDetails api
 */
data class UsageDataResponse(val success: Boolean, val result: ResultData)

data class ResultData(
    val records: ArrayList<RecordsData>, val total: Int
)

data class RecordsData(
    @SerializedName("_id") val id: Int, val quarter: String,
    val volume_of_mobile_data: Double
)
