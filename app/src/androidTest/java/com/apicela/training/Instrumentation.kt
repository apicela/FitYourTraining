package com.apicela.training

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.apicela.training.builder.ExecutionBuilder
import com.apicela.training.builder.ExerciseBuilder
import com.apicela.training.data.Database
import com.apicela.training.data.dao.ExecutionDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class Instrumentation {

    private lateinit var db: Database
    private lateinit var executionDao: ExecutionDao
    val exerciseBuilder = ExerciseBuilder()

    @Before
    fun setUp() {
        // Configura o banco de dados na memória
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .build()
        executionDao = db.executionDao()
    }

    @After
    fun tearDown() {
        db.close() // Fecha o banco de dados após cada teste
    }

    @Test
    fun testGetAllExercises() {
        // Exemplo de como usar o banco de dados na memória em um teste
        val oldResult = runBlocking { db.exerciseDao().getAll() }
        val newExercise = exerciseBuilder.build();
        runBlocking { db.exerciseDao().insert(newExercise) }
        val newResult = runBlocking { db.exerciseDao().getAll() }
        Log.d("Instrumentation", "${newExercise}")
        assertNotEquals(oldResult.size, newResult.size)
    }

    @Test
    fun testGetAllExecutions() {
        val newExercise = exerciseBuilder.build();
        runBlocking { db.exerciseDao().insert(newExercise) }
        val oldResult = runBlocking { db.executionDao().getAll() }
        val execution = ExecutionBuilder().exerciseId(newExercise.id)
            .build();
        runBlocking { db.executionDao().insert(execution) }
        val newResult = runBlocking { db.executionDao().getAll() }
        Log.d("Instrumentation", "${execution}")
        assertNotEquals(oldResult.size, newResult.size)
    }


}