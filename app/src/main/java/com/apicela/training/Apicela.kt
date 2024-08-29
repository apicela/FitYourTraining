package com.apicela.training

import android.app.Application
import com.apicela.training.data.DataManager
import com.apicela.training.models.Exercise
import com.apicela.training.services.ExerciseService
import com.apicela.training.ui.activitys.HomeActivity
import com.apicela.training.utils.preferences.SharedPreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Apicela : Application() {
    lateinit var exerciseService: ExerciseService
    lateinit var sharedPreferencesHelper : SharedPreferencesHelper

    companion object {
        var REST_TIMING = ""
        @Volatile
        private lateinit var instance: Apicela

        fun getInstance(): Apicela {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;
        HomeActivity.DATABASE = DataManager.getDatabase(this)
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        exerciseService = ExerciseService()
        setUpFirstStart()
        REST_TIMING = sharedPreferencesHelper.clockTime
    }

    fun updateClockTime(newTime: String) {
        REST_TIMING = newTime
        sharedPreferencesHelper.clockTime = newTime
    }

    fun setUpFirstStart() {
        CoroutineScope(Dispatchers.IO).launch {
            sharedPreferencesHelper.initializeOnce()
            val listExercises = exerciseService.getAllExercises()
            val itemsToAdd =
                Exercise.listaExercises.filter { obj -> listExercises.none { it.name == obj.name } }
            itemsToAdd.forEach {
                exerciseService.addExerciseToDatabase(it)
            }
        }
    }

}