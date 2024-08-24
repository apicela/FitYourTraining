package com.apicela.training.services

import android.util.Log
import com.apicela.training.data.Database
import com.apicela.training.models.Division
import com.apicela.training.models.Exercise
import com.apicela.training.ui.activitys.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ExerciseService() {
    private val db: Database = HomeActivity.DATABASE
    val divisionService = DivisionService()


    suspend fun removeExerciseFromDivision(division_id: String, exercise_id: String) {
        var division = divisionService.getDivisionById(division_id)
        val listOfExercises = division!!.listOfExercises as MutableList<Exercise>
        listOfExercises.removeIf { it.id == exercise_id }
        division.listOfExercises = listOfExercises
        divisionService.updateDivisionObject(division)
    }

    suspend fun updateExercise(exercise : Exercise){
        withContext(Dispatchers.IO) {
            db.exerciseDao().update(exercise)
        }
    }

    suspend fun deleteExerciseById(id: String) {
        withContext(Dispatchers.IO) {
            db.exerciseDao().deleteById(id)
        }
    }

    suspend fun notifyListExercisesFromDivisionChanged(
        divisionId: String,
        listOfExercises: List<Exercise>
    ) {
        withContext(Dispatchers.IO) {
            divisionService.updateListOfExercises(divisionId, listOfExercises)
        }
    }

    suspend fun addExerciseToDatabase(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            db.exerciseDao().insert(exercise)
        }
        Log.d("Exercise", "Exercise added to database")
    }

    suspend fun getExerciseById(id: String): Exercise {
        return withContext(Dispatchers.IO) {
            db.exerciseDao().getExerciseById(id)
        }
    }

    suspend fun getAllExercises(): List<Exercise> {
        return withContext(Dispatchers.IO) {
            db.exerciseDao().getAllExercises()
        }
    }

    suspend fun exerciseListToMap(division_id: String? = null): Map<String, List<Exercise>> {
        val exerciseList = if (division_id == null) {
            runBlocking { getAllExercises() }
        } else {
            val division = divisionService.getDivisionById(division_id)
            division?.listOfExercises
        }
        if (exerciseList != null) {
            return exerciseList.groupBy { exercise ->
                exercise.muscleType.toString()
            }
        }
        return emptyMap()
    }

    public fun getExerciseListFromDivision(divisionId: String?): List<Exercise> {
        val division = getDivision(divisionId)
        return if (division != null && division.listOfExercises.isNotEmpty()) {
            division.listOfExercises
        } else listOf()
    }

    fun getDivision(divisionId: String?): Division? {
        return if (divisionId != null) {
            runBlocking { divisionService.getDivisionById(divisionId) }
        } else null
    }

//    fun addExerciseToDivision(exerciseName: String, image: String, muscleType: Muscle) {
//        val exerciseItem = Exercise(exerciseName, image, muscleType)
//        val lista = DataManager.loadExerciseItems()
//        lista.add(exerciseItem)
//        DataManager.saveExerciseItems(lista)
//        Log.d("Exercise", "Exercise added to workout")
//    }

}