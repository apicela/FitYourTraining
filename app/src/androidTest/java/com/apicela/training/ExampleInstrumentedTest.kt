package com.apicela.training

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.apicela.training.data.DataManager
import com.apicela.training.data.Database
import com.apicela.training.data.dao.ExecutionDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var app: Context
    private lateinit var db: Database
    private lateinit var executionDao: ExecutionDao
    @Before
    fun setUp() {
        // Configura a instância do Apicela
        app = InstrumentationRegistry.getInstrumentation().targetContext
        db = DataManager.getDatabase(app)
        executionDao = db.executionDao()
    }
    @After
    fun teardown() {
        db.close() // Fecha o banco de dados após cada teste
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.apicela.training", appContext.packageName)
    }

    @Test
    fun testQuery(){
        //  Apicela()
        val exerciseId = "18d2e426-dc7b-43e3-b8b6-072ea4389a11"
        val sixMonthsAgo = 1726353090560
        val results = db.executionDao().getKgDataForPastSixMonths(exerciseId,sixMonthsAgo)
        Log.d("Statistics", "exerciseId: ${exerciseId}, sixMonths: ${sixMonthsAgo}, result : ${results}")
        assertNotEquals(0, results.size)

    }

    @Test
    fun testGetAll(){
        Log.d("Statistics", " db : ${db}")
        val results = runBlocking {  db.executionDao().getAllExecution() }
       Log.d("Statistics", " result : ${results}")
        assertNotEquals(0, results.size)

    }
}