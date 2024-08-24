package com.apicela.training.utils.converter

import androidx.room.TypeConverter
import com.apicela.training.models.Division
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DivisionListConverter {
    @TypeConverter
    fun fromString(value: String): List<Division> {
        val listType = object : TypeToken<List<Division>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Division>): String {
        return Gson().toJson(list)
    }
}