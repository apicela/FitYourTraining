package com.apicela.training.service

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.apicela.training.builder.ExecutionBuilder
import com.apicela.training.builder.ExerciseBuilder
import com.apicela.training.data.Database
import com.apicela.training.models.Exercise
import com.apicela.training.models.extra.ExecutionInfo
import com.apicela.training.services.ExecutionService
import com.apicela.training.services.ExerciseService
import com.apicela.training.ui.utils.Components
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Date
import java.util.UUID

class ExerciseServiceTest {
    private lateinit var db: Database
    private lateinit var exerciseService: ExerciseService
    private lateinit var executionService: ExecutionService
    private lateinit var exercise: Exercise

    @Before
    fun setUp() {
        // Configura o banco de dados na memória
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .build()
        exerciseService = ExerciseService(db)
        executionService = ExecutionService(db)
        exercise = ExerciseBuilder().build();
        runBlocking { ExerciseService().addExerciseToDatabase(exercise) }
    }

    @Test
    fun testPastMonths(){
        for (i in 5..9) {
            val dateString = "10/0${i}/2024"
            val date: Date = Components.createDateByString(dateString)
            val execution = ExecutionBuilder()
                .exerciseId(exercise.id)
                .date(date)
                .kg(1f)
                .build();
            runBlocking {        executionService.addExecutionToDatabase(execution) }

            val execution1 = ExecutionBuilder()
                .exerciseId(exercise.id)
                .date(date)
                .kg(500f)
                .repetitions(1)
                .build();
            runBlocking {    executionService.addExecutionToDatabase(execution1) }
            execution1.id = UUID.randomUUID().toString();
            runBlocking {    executionService.addExecutionToDatabase(execution1) }
            execution1.id = UUID.randomUUID().toString();
            runBlocking {    executionService.addExecutionToDatabase(execution1) }
            execution1.id = UUID.randomUUID().toString();
            runBlocking {    executionService.addExecutionToDatabase(execution1) }

            val execution2 = ExecutionBuilder()
                .exerciseId(exercise.id)
                .date(date)
                .kg(500f)
                .repetitions(6)
                .build();
            runBlocking {        executionService.addExecutionToDatabase(execution2) }
            execution2.id = UUID.randomUUID().toString();
            runBlocking {        executionService.addExecutionToDatabase(execution2) }
            execution2.id = UUID.randomUUID().toString();
            runBlocking {        executionService.addExecutionToDatabase(execution2) }

        }

        val list : List<ExecutionInfo> = runBlocking { exerciseService.getExerciseInfoPastMonths(exercise.id, 6)}
//        val convertedList = list.map{ it -> it.convertToModeOrMaxWeight()}
        Log.d("ExerciseServiceTest", "$list")
        val executionRawMax = list[0].convertToModeOrMaxWeight()
//        Log.d("ExerciseServiceTest", "$convertedList")
        Assert.assertNotEquals(0, list.size)
        Assert.assertEquals(500f, executionRawMax?.kg)
        Assert.assertEquals(1f, executionRawMax?.repetitions)
//        Assert.assertNotEquals(0, convertedList.size)

    }
    @After
    fun tearDown() {
        db.close() // Fecha o banco de dados após cada teste
    }
}