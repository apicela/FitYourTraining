package com.apicela.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apicela.training.models.Workout

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout")
    suspend fun getAll(): List<Workout>

    @Query("SELECT * FROM workout WHERE id = :workoutId")
    suspend fun getById(workoutId: String): Workout

    @Insert
    suspend fun insert(workout: Workout)

    @Query("DELETE FROM workout WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(workout: Workout)
}