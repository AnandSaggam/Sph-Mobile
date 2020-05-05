package com.dlminfosoft.sphmobile.model

data class UsageDataResponse(val success: Boolean, val result: ResultData)

data class ResultData(
    val records: ArrayList<RecordsData>, val total: Int, val offset: Int = 0, val limit: Int = 0
)

data class RecordsData(val _id: Int, val quarter: String, val volume_of_mobile_data: String)
