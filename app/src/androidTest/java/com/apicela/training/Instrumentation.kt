package com.apicela.training

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.apicela.training.data.Database
import com.apicela.training.data.dao.ExecutionDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class Instrumentation {

    private lateinit var inMemoryDb: Database
    private lateinit var executionDao: ExecutionDao

    @Before
    fun setUp() {
        // Configura o banco de dados na memória
        val context = ApplicationProvider.getApplicationContext<Context>()
        inMemoryDb = Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .build()
        executionDao = inMemoryDb.executionDao()
    }

    @After
    fun tearDown() {
        inMemoryDb.close() // Fecha o banco de dados após cada teste
    }

    @Test
    fun testGetAll() {
        // Exemplo de como usar o banco de dados na memória em um teste
        val results = runBlocking { executionDao.getAllExecution() }
        Log.d("Statistics", "result : $results")
        assertNotEquals(0, results.size)
    }

}