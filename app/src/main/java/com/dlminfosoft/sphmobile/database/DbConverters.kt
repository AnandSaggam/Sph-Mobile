package com.dlminfosoft.sphmobile.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.TreeMap

/*
* This class used for converting Object to String and vice versa when storing and retrieving from database
*/
class DbConverters {

    @androidx.room.TypeConverter
    fun fromString(value: String): TreeMap<String, Double> {
        val type: Type = object : TypeToken<TreeMap<String, Double>>() {}.type
        return Gson().fromJson(value, type)
    }

    @androidx.room.TypeConverter
    fun fromTreeMap(treeMapData: TreeMap<String, Double>): String {
        return Gson().toJson(treeMapData)
    }
}