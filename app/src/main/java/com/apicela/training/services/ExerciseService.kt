package com.apicela.training.services

import android.util.Log
import com.apicela.training.data.Database
import com.apicela.training.models.Division
import com.apicela.training.models.Exercise
import com.apicela.training.models.extra.ExecutionInfo
import com.apicela.training.models.extra.Info
import com.apicela.training.ui.activitys.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class ExerciseService(private val db: Database = HomeActivity.DATABASE) {
    val divisionService = DivisionService()


    suspend fun removeExerciseFromDivision(divisionId: String, exerciseId: String) {
        var division = divisionService.getDivisionById(divisionId)
        val listOfExercises = division!!.listOfExercises as MutableList<String>
        listOfExercises.removeIf { it == exerciseId }
        division.listOfExercises = listOfExercises
        divisionService.updateDivisionObject(division)
    }

    suspend fun updateExercise(exercise: Exercise) {
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
        listOfExercises: List<String>
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
            db.exerciseDao().getById(id)
        }
    }

    suspend fun getAllExercises(): List<Exercise> {
        return withContext(Dispatchers.IO) {
            db.exerciseDao().getAll()
        }
    }

    suspend fun exerciseListToMap(divisionId: String? = null): Map<String, List<Exercise>> {
        val exerciseList = if (divisionId == null) {
            runBlocking { getAllExercises() }
        } else {
            val division = divisionService.getDivisionById(divisionId)
            division?.listOfExercises?.mapNotNull { exerciseId ->
                db.exerciseDao().getById(exerciseId)
            } ?: emptyList()
        }
        if (exerciseList.isNotEmpty()) {
            return exerciseList.groupBy { exercise ->
                exercise.muscleType.toString()
            }
        }
        return emptyMap()
    }

    public suspend fun getExerciseListFromDivision(divisionId: String?): List<Exercise> {
        val division = getDivision(divisionId)
        return if (division != null && division.listOfExercises.isNotEmpty()) {
            runBlocking {
                division.listOfExercises.mapNotNull { id ->
                    db.exerciseDao().getById(id)
                }
            }
        } else listOf()
    }

    fun getDivision(divisionId: String?): Division? {
        return if (divisionId != null) {
            runBlocking { divisionService.getDivisionById(divisionId) }
        } else null
    }

    suspend fun getExerciseInfoPastMonths(exerciseId: String, monthsAgo: Int): List<ExecutionInfo> {
        // Calcular a data em milissegundos referente a monthsAgo
        val monthsAgoDate = LocalDate.now().minus(monthsAgo.toLong(), ChronoUnit.MONTHS)
        val monthsAgoInMillis =
            monthsAgoDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        // Obter a lista de execuções do banco de dados
        val executionsPastMonthAsExecutionRawList = withContext(Dispatchers.IO) {
            db.executionDao().getExecutionsForPastMonths(exerciseId, monthsAgoInMillis)
        }

        // Agrupar por mês e mapear para o tipo ExecutionInfo
        val list = executionsPastMonthAsExecutionRawList.groupBy { it.month }
            .map { (monthYear, infos) ->
                ExecutionInfo(
                    month = monthYear,
                    list = infos.map { Info(it.kg, it.repetitions) }
                )
            }
        return list
    }

}