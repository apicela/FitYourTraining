package com.apicela.training.service

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.apicela.training.builder.ExecutionBuilder
import com.apicela.training.builder.ExerciseBuilder
import com.apicela.training.data.Database
import com.apicela.training.models.Exercise
import com.apicela.training.services.ExecutionService
import com.apicela.training.services.ExerciseService
import com.apicela.training.ui.utils.Components
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class ExecutionServiceTest {
    private lateinit var db: Database
    private lateinit var executionService: ExecutionService
    private lateinit var exercise: Exercise

    @Before
    fun setUp() {
        // Configura o banco de dados na memória
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .build()
        executionService = ExecutionService(db)
        exercise = ExerciseBuilder().build();
        runBlocking { ExerciseService().addExerciseToDatabase(exercise) }

    }

    @After
    fun tearDown() {
        db.close() // Fecha o banco de dados após cada teste
    }

    @Test
    fun testExecutionsByDate() {

        for (i in 5..9) {
            val dateString = "10/0${i}/2024"
            val date: Date = Components.createDateByString(dateString)
            val execution = ExecutionBuilder()
                .exerciseId(exercise.id)
                .date(date)
                .build();
            runBlocking { db.executionDao().insert(execution) }

            val execution1 = ExecutionBuilder()
                .exerciseId(exercise.id)
                .date(date)
                .build();
            runBlocking { db.executionDao().insert(execution1) }
        }

        val result =
            runBlocking { executionService.getExecutionsFromExerciseIdPastMonths(exercise.id, 6) }

        Log.d("Instrumentation", " result: ${result}}")
        assertNotEquals(0, result.size)
    }
}