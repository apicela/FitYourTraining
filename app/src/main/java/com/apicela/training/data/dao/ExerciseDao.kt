package com.apicela.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apicela.training.models.Exercise

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    suspend fun getAll(): List<Exercise>

    @Query("SELECT * FROM exercise WHERE id = :id")
    suspend fun getById(id: String): Exercise

    @Query("DELETE FROM Exercise WHERE id = :id")
    suspend fun deleteById(id: String)

    @Insert
    suspend fun insert(exercise: Exercise)

    @Update
    suspend fun update(workout: Exercise)
}

