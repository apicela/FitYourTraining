package com.apicela.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apicela.training.models.Execution
import com.apicela.training.models.extra.ExecutionRaw


@Dao
interface ExecutionDao {

    @Query("SELECT * FROM Execution")
    suspend fun getAll(): List<Execution>

    @Query("SELECT * FROM execution WHERE strftime('%d/%m/%Y', date / 1000, 'unixepoch') =  :date")
    suspend fun getAllExecutionFromDate(date: String): List<Execution>

    @Query("SELECT * FROM execution WHERE id = :id")
    suspend fun getById(id: String): Execution

    @Query("SELECT * FROM Execution WHERE exercise_id = :exerciseId ORDER BY date DESC")
    suspend fun getAllExecutionFromExercise(exerciseId: String): List<Execution>

    @Query("SELECT * FROM Execution WHERE exercise_id = :id ORDER BY date DESC LIMIT 1")
    suspend fun getLastInserted(id: String): Execution?

    @Insert
    suspend fun insert(execution: Execution)

    @Update
    suspend fun update(execution: Execution)

    @Query("DELETE FROM Execution WHERE id = :id")
    suspend fun deleteById(id: String)


    @Query(
        """
        SELECT 
            strftime('%Y-%m', date / 1000, 'unixepoch') AS month, 
            kg, 
            repetitions 
        FROM Execution 
        WHERE exercise_id = :exerciseId AND date >= :monthsAgoInMillis 
        ORDER BY date ASC
    """
    )
    suspend fun getExecutionsForPastMonths(
        exerciseId: String,
        monthsAgoInMillis: Long
    ): List<ExecutionRaw>

}