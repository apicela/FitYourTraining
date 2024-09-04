package com.apicela.training.utils.converter

import androidx.room.TypeConverter
import com.apicela.training.models.Exercise
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExerciseListConverter {
    @TypeConverter
    fun fromString(value: String): List<Exercise> {
        val listType = object : TypeToken<List<Exercise>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Exercise>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromGenericList(list: List<Any>): String { // Renamed method
        return Gson().toJson(list)
    }
}