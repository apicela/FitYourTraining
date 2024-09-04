package com.apicela.training.utils.converter

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DivisionListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return try {
            Gson().fromJson(value, listType)
        } catch (e: Exception) {
            // Log de erro e retorno nulo se falhar
            Log.e("DivisionListConverter", "Error converting JSON to List<String>: ${e.message}," +
                    " value : $value , listType : $listType ")
            null
        }
    }
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return Gson().toJson(list)
    }
}