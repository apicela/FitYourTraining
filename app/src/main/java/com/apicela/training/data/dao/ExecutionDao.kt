package com.apicela.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apicela.training.models.Execution
import com.apicela.training.models.extra.MonthKgMode
import java.util.Date


@Dao
interface ExecutionDao {

    @Query("SELECT * FROM Execution")
    fun getAllExecution(): List<Execution>

    @Query("SELECT * FROM execution WHERE strftime('%d/%m/%Y', date / 1000, 'unixepoch') =  :date")
    fun getAllExecutionFromDate(date: String): List<Execution>

    @Query("SELECT * FROM execution WHERE id = :id")
    fun getExecutionById(id: String): Execution

    @Query("SELECT * FROM Execution WHERE exercise_id = :exerciseId ORDER BY date DESC")
    fun getAllExecutionFromExercise(exerciseId: String): List<Execution>

    @Query("SELECT * FROM Execution WHERE exercise_id = :id ORDER BY date DESC LIMIT 1")
    fun getLastInsertedExecution(id: String): Execution?

    @Insert
    fun insert(execution: Execution)

    @Update
    fun update(execution: Execution)

    @Query("DELETE FROM Execution WHERE id = :id")
    fun deleteById(id: String)


    @Query("""
    SELECT date(date, 'yyyy-MM') AS month, kg
    FROM Execution
    WHERE exercise_id = :exerciseId AND date >= :sixMonthsAgo
""")
    fun getKgDataForPastSixMonths(exerciseId: String, sixMonthsAgo: Long): List<MonthKgMode>

}