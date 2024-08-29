package com.apicela.training.services

import android.util.Log
import com.apicela.training.data.Database
import com.apicela.training.models.Execution
import com.apicela.training.models.extra.ExecutionInfo
import com.apicela.training.models.extra.Info
import com.apicela.training.ui.activitys.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExecutionService() {
    private val db: Database = HomeActivity.DATABASE
    val exerciseService: ExerciseService = ExerciseService()

    suspend fun addExecutionToDatabase(execution: Execution) {
        withContext(Dispatchers.IO) {
            db.executionDao().insert(execution)
        }
        Log.d("Exercise", "Exercise added to database")
    }

    suspend fun deleteById(id: String) {
        withContext(Dispatchers.IO) {
            db.executionDao().deleteById(id)
        }
    }

    suspend fun findExecutionsListByExerciseId(exerciseId: String): List<Execution> {
        return withContext(Dispatchers.IO) {
            db.executionDao().getAllExecutionFromExercise(exerciseId)
        }
    }

    suspend fun findExecutionsListByDate(date: Date): List<Execution> {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = format.format(date)
        Log.d("Execution", "date: ${formattedDate}")
        return withContext(Dispatchers.IO) {
            db.executionDao().getAllExecutionFromDate(formattedDate.toString())
        }
    }

    suspend fun groupExercisesExecutionByDateIntoString(date: Date): List<String> {
        val items = runBlocking { findExecutionsListByDate(date) }
        val listOfExecutions = mutableListOf("");
        val groupedItems = items.groupBy { it.exercise_id }
        for (exerciseId in groupedItems.keys) {
            val exerciseName = exerciseService.getExerciseById(exerciseId).name
            listOfExecutions.add(
                "${exerciseName.lowercase()} : ${
                    joinExerciseListToString(
                        groupedItems[exerciseId]!!
                    )
                }"
            )
        }
        return listOfExecutions
    }

    fun executionListToMap(exerciseId: String): Map<String, List<Execution>> {
        val executionList = runBlocking { findExecutionsListByExerciseId(exerciseId) }
        return executionList.groupBy { execution ->
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(execution.date)
        }
    }

    suspend fun getAll(): List<Execution> {
        return withContext(Dispatchers.IO) {
            db.executionDao().getAll()
        }
    }

    suspend fun updateExecutionObject(execution: Execution) {
        withContext(Dispatchers.IO) {
            db.executionDao().update(execution)
        }
    }

    suspend fun getLastInsertedExecution(id: String): Execution? {
        return withContext(Dispatchers.IO) {
            db.executionDao().getLastInserted(id)
        }
    }

    suspend fun getExecutionById(id: String): Execution? {
        return withContext(Dispatchers.IO) {
            db.executionDao().getById(id)
        }
    }

    fun joinExerciseListToString(list: List<Execution>): String {
        val response = StringBuilder()
        list.forEach { execution ->
            if (execution.isCardio) {
                response.append(", ${execution.repetitions} minutos")
            } else {
                response.append(", ${execution.repetitions}x${execution.kg}kg")
            }
        }
        return response.drop(2).toString()
    }

//    fun getKgDataForPastSixMonths(exerciseId: String, date: Date){
//        Long timestamp =
//    }

    suspend fun getExecutionsFromExerciseIdPastMonths(exerciseId: String, monthsAgo: Int): List<ExecutionInfo> {
        val sixMonthsAgo = LocalDate.now().minus(6, ChronoUnit.MONTHS)
        val monthsAgoInMillis = sixMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val listOfExecution = runBlocking { db.executionDao().getAll().size }
        Log.d("ExecutionService", "listOfExec: ${listOfExecution}")
        return withContext(Dispatchers.IO) {
            val executionsPastMonthAsExecutionRawList = db.executionDao().getExecutionsForPastMonths(exerciseId, monthsAgoInMillis)
            val executionsPastMonthAsExecutionRawList1 = runBlocking {  db.executionDao().getExecutionsForPastMonths(exerciseId) }
            Log.d("ExecutionService", "common: ${executionsPastMonthAsExecutionRawList}")
            Log.d("ExecutionService", "1: ${executionsPastMonthAsExecutionRawList1}")
            val list = executionsPastMonthAsExecutionRawList.groupBy { it.month }
                .map { (monthYear, infos) ->
                    ExecutionInfo(
                        month = monthYear,
                        list = infos.map { Info(it.kg, it.repetitions) }
                    )
                }
            Log.d("ExecutionService", "${list}")
            return@withContext list
        }
    }


}