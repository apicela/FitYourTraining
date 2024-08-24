package com.apicela.training.utils.preferences

import android.content.Context
import android.content.SharedPreferences
import com.apicela.training.models.Exercise
import com.apicela.training.services.ExerciseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedPreferencesHelper(private val context: Context) {
    private val PREF_NAME = "user_prefs_apicela"
    private val PREF_INITIALIZED = "apicela_db_started"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("ApicelaPrefs", Context.MODE_PRIVATE)

    suspend fun initializeOnce() {
        withContext(Dispatchers.IO) {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val initialized = prefs.getBoolean(PREF_INITIALIZED, false)
            if (!initialized) {
                val exerciseService: ExerciseService = ExerciseService()
                Exercise.listaExercises.forEach {
                    exerciseService.addExerciseToDatabase(it)
                }
                prefs.edit().putBoolean(PREF_INITIALIZED, true).apply()
            }
        }
    }

    var clockTime: String
        get() = sharedPreferences.getString("REST_TIME", "01:30") ?: "01:30"
        set(value) {
            sharedPreferences.edit().putString("REST_TIME", value).apply()
        }
}